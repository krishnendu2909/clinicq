// import React, { useEffect, useState } from 'react';

// import { FaHospitalUser,FaStethoscope,FaHeart,FaBone } from 'react-icons/fa6';

// //import axios from 'axios';

// import Swal from 'sweetalert2';

// import axiosInstance from '../../../services/axiosInstance';

// interface BookProps {

//   onNavigate: (tab: 'book' | 'my' | 'history') => void;

// }

// type Doctor = {

//   id: number;

//   name: string;

//   location: string;

//   description: string;

// };

// const BookAppointment: React.FC<BookProps> = ({ onNavigate }) => {

//   // --- STATE MANAGEMENT ---

//   const [selectedSpec, setSelectedSpec] = useState("GENERAL");

//   const [availableSlots, setAvailableSlots] = useState<any[]>([]);

//   // Look for this line near the top of the component

//   const [activeSlot, setActiveSlot] = useState<string | null>(null);

//   // Update your state declaration like this:


 

//   // Get today's date in YYYY-MM-DD format

//   const today = new Date().toLocaleDateString('en-CA');

//   const [selectedDate, setSelectedDate] = useState(today);

//   //const [selectedDoctor, setSelectedDoctor] = useState("Dr. Angela Lee");

//   const [doctors, setDoctors] = useState<Doctor[]>([]);

//   const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);


 

//   // US 03: Reschedule States

//   const [reschedulingId, setReschedulingId] = useState<number | null>(null);

//   const [rescheduleDate, setRescheduleDate] = useState("");

//   const [rescheduleTime, setRescheduleTime] = useState("");

//   const [rescheduleSlots, setRescheduleSlots] = useState<any[]>([]);

//   const[rescheduleDoctor,setRescheduleDoctor]=useState<Doctor|null>(null);

//   // US 02: List State

//   const [myList, setMyList] = useState<any[]>([]);


 

//   const specs = [

//     // name MUST match your Java Enum exactly

//     { name: "GENERAL", icon: "🏥", color: "#00f2fe" },    // Cyan/Neon Blue

//     { name: "CARDIOLOGY", icon: "❤️", color: "#ff75c3" }, // Neon Pink

//     { name: "ORTHOPEDICS", icon: "🦴", color: "#ffa64d" },// Neon Orange

//     { name: "PEDIATRICS", icon: "👶", color: "#7df9ff" }  // Electric Blue

//   ];

//   // const specs = [

//   //   // name MUST match your Java Enum exactly

//   //   { name: "GENERAL", icon: FaHospitalUser, color: "#00f2fe" },    // Cyan/Neon Blue

//   //   { name: "CARDIOLOGY", icon: FaHeart, color: "#ff75c3" }, // Neon Pink

//   //   { name: "ORTHOPEDICS", icon: FaBone, color: "#ffa64d" },// Neon Orange

//   //   { name: "PEDIATRICS", icon: FaStethoscope, color: "#7df9ff" }  // Electric Blue

//   // ];


 

//   // --- 1. RESCHEDULE CLICK HANDLER (Sibling to handleBooking) ---

//   const handleRescheduleClick = async (app: any) => {

//     setReschedulingId(app.id);

//     setRescheduleDoctor(app.doctor);

//     setRescheduleDate(app.timeSlot?.slotDate);


 

//     setRescheduleTime("");


 

//     const doctorId = app.doctor?.id;

//     const slotDate = app.timeSlot?.slotDate;

//     if (doctorId && slotDate) {


 

//       try {

//         const response = await axiosInstance.get(

//           `/clinicq/patient/slots?doctorId=${doctorId}&date=${slotDate}`,


 

//         );

//         // KEY CHANGE: Use the new state variable here

//         setRescheduleSlots(response.data);

//       } catch (err) {

//         console.error("Manual fetch failed:", err);

//         setRescheduleSlots([]);

//       }

//     }

//   };


 

//   // --- HELPERS ---

//   const formatDate = (dateStr: string) => {

//     if (!dateStr) return "";

//     const date = new Date(dateStr);

//     return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });

//   };


 

//   const handleBooking = async () => {

//     // 1. Validation


 

//     if (!selectedDoctor) {


 

//       Swal.fire({

//         title: 'Doctor Not Selected',

//         text: 'Please choose a doctor before booking.',

//         icon: 'warning',

//         background: '#111',

//         color: '#fff',

//         confirmButtonColor: '#0dcaf0'

//       });

//       return;


 

//     }


 

//     if (!activeSlot) {

//       Swal.fire({

//         title: 'Select a Time',

//         text: 'Please pick an available time slot to continue.',

//         icon: 'info',

//         background: '#111',

//         color: '#fff',

//         confirmButtonColor: '#0dcaf0'

//       });

//       return;

//     }

//     // Find the slot ID from your availableSlots array

//     // We need the ID, but your button click currently only sets the startTime string

//     const selectedSlot = availableSlots.find(s => s.startTime === activeSlot);



 

//     if (!selectedSlot) return alert("Invalid slot selection.");


 

//     // 2. Construct the request body

//     const bookingData = {

//       patientId: 1, // Currently hardcoded; later you'll get this from login session

//       doctorId: selectedDoctor.id,

//       slotId: selectedSlot?.id,

//       reason: "General Consultation" // You could add an input field for this later

//     };


 

//     try {

//       // 3. Make the API Call

//       const response = await axiosInstance.post("/clinicq/patient/appointment", bookingData);


 

//       if (response.status === 201) {

//         Swal.fire({

//           title: 'Appointment Booked!',

//           text: `Your session with ${selectedDoctor?.name} is confirmed.`,

//           icon: 'success',

//           background: '#111',       // Matches your dark background

//           color: '#fff',            // White text

//           confirmButtonColor: '#0dcaf0', // Your Info/Cyan color

//           iconColor: '#0dcaf0',

//           customClass: {

//             popup: 'rounded-4 border border-secondary shadow-lg'

//           }

//         });


 

//         // 4. Update the local "My List" UI

//         const newApp = {

//           id: Date.now(),

//           dr: selectedDoctor.name,

//           date: formatDate(selectedDate),

//           time: activeSlot.substring(0, 5),

//           status: "Booked",

//           color: "info"

//         };


 

//         //setMyList([newApp, ...myList]);

//         fetchUpcomingAppointments();

//         // 5. Cleanup UI

//         setActiveSlot(null);



 

//         // 6. Refresh available slots (since one is now booked)

//         fetchSlots();

//       }

//     }


 

//     catch (err: any) {

//       console.error("Booking Error:", err);

//       Swal.fire({

//         title: 'Booking Failed',

//         text: err.response?.data?.errorMessage || "Please try again later.",

//         icon: 'error',

//         background: '#111',

//         color: '#fff',

//         confirmButtonColor: '#dc3545' // Red color for errors

//       });

//     }

//   };


 

//   const handleApplyReschedule = async () => {


 

//     if (!reschedulingId || !rescheduleTime) {

//       Swal.fire({ icon: 'warning', title: 'Select a time slot', background: '#111', color: '#fff' });

//       return;

//     }


 

//     try {

//       // API Call using @RequestParam structure

//       const response = await axiosInstance.put(

//         `/clinicq/patient/appointments/reschedule`,

//         null, // No body

//         {

//           params: {

//             appointmentId: reschedulingId,

//             newSlotId: rescheduleTime // rescheduleTime state now holds the Slot ID

//           }

//         }

//       );


 

//       if (response.status === 200) {

//         Swal.fire({

//           title: 'Updated!',

//           text: 'Appointment rescheduled successfully.',


 

//           icon: 'success',

//           background: '#111', color: '#fff', confirmButtonColor: '#0dcaf0'

//         });


 

//         // Reset Form & Refresh List

//         setReschedulingId(null);

//         setRescheduleTime("");

//         fetchUpcomingAppointments(); // Re-syncs "My List" with DB

//         fetchSlots();               // Re-syncs the left-side availability

//       }

//     } catch (err: any) {

//       Swal.fire({ icon: 'error', title: 'Failed to Reschedule', text: err.response?.data?.errorMessage, background: '#111', color: '#fff' });

//     }

//   };

//   const handleCancel = async (appointmentId: number) => {

//     // 1. Confirm with the user

//     const result = await Swal.fire({

//       title: 'Are you sure?',

//       text: "This will cancel your appointment and free up the slot.",

//       icon: 'warning',

//       showCancelButton: true,

//       confirmButtonColor: '#d33',

//       cancelButtonColor: '#3085d6',

//       confirmButtonText: 'Yes, cancel it!',

//       background: '#111', color: '#fff'

//     });


 

//     if (result.isConfirmed) {

//       try {

//         // 2. Call the DELETE API

//         const response = await axiosInstance.delete(

//           `/clinicq/patient/appointments/cancel/${appointmentId}`

//         );


 

//         if (response.status === 200 || response.status === 204) {

//           // --- CRITICAL CHANGE: Remove from local state immediately ---

//           setMyList(prevList => prevList.filter(app => app.id !== appointmentId));


 

//           // If we were currently trying to reschedule this specific one, close the form

//           if (reschedulingId === appointmentId) {

//             setReschedulingId(null);

//           }

//           Swal.fire({

//             title: 'Cancelled!',

//             text: 'The slot is now available again.',

//             icon: 'success',

//             background: '#111', color: '#fff', confirmButtonColor: '#0dcaf0'

//           });


 

//           // 3. REFRESH EVERYTHING

//           //fetchUpcomingAppointments(); // Removes it from the right-side list

//           fetchSlots();               // Makes the slot button clickable again on the left

//         }

//       } catch (err: any) {

//         console.error("Cancellation Error:", err);

//         Swal.fire({

//           icon: 'error',

//           title: 'Error',

//           text: 'Could not cancel appointment. Please try again.',

//           background: '#111', color: '#fff'

//         });

//       }

//     }

//   };


 

//   const fetchUpcomingAppointments = () => {

//     const patientId = 1; // Use the same ID you use for booking

//     axiosInstance.get(`/clinicq/patient/appointments/upcoming`)

//       .then(res => {

//         // Map the backend DTOs to your frontend UI format if needed

//         setMyList(res.data);

//       })

//       .catch(err => console.error("Error fetching appointments:", err));

//   };


 

//   // Call it on initial load too

//   useEffect(() => {

//     fetchUpcomingAppointments();

//   }, []);


 

//   useEffect(() => {

//     const fetchDoctors = async () => {

//       try {

//         // Matches your @GetMapping("/doctors") and @RequestParam department

//         const response = await axiosInstance.get(`/clinicq/patient/doctors`, {

//           params: { department: selectedSpec }

//         });


 

//         console.log("Doctors found:", response.data); // Open F12 console to verify names

//         setDoctors(response.data);

//         setSelectedDoctor(null);

//       } catch (err) {

//         console.error("Error fetching doctors:", err);

//         setDoctors([]);

//       }

//     };


 

//     if (selectedSpec) fetchDoctors();

//   }, [selectedSpec]);


 

//   useEffect(() => {


 

//     if (selectedDoctor && selectedDate) {


 

//         axiosInstance.get(`/clinicq/patient/slots`,{

//           params:{

//             doctorId:selectedDoctor.id,

//             date:selectedDate,

//           },

//         })

//         .then((res) => setAvailableSlots(res.data))

//         .catch(err => console.error(err));

//     }

//   }, [selectedDoctor, selectedDate]);


 

//   useEffect(()=>{

//     if(rescheduleDoctor && rescheduleDate){


 

//       axiosInstance.get(`/clinicq/patient/slots`,{

//                 params:{

//                   doctorId:rescheduleDoctor.id,

//                   date:rescheduleDate,

//                 },

//               })

//       .then((res)=>{

//         setRescheduleSlots(res.data);

//       })

//       .catch(err=>console.error("Error fetching slots: ",err));

//     }

//   },[rescheduleDoctor,rescheduleDate]);

//   // Add this function inside your component

//   const fetchSlots = () => {

//     if (selectedDoctor && selectedDate) {

//          axiosInstance.get(`/clinicq/patient/slots`,{

//                 params:{

//                   doctorId:selectedDoctor.id,

//                   date:selectedDate,

//                 },

//               })    

//         .then((res) => setAvailableSlots(res.data))

//         .catch(err => console.error(err));

//     }

//   };


 

//   // Use it in useEffect

//   useEffect(() => {

//     fetchSlots();

//   }, [selectedDoctor, selectedDate]);

//   useEffect(() => {

//     const fetchRescheduleSlots = async () => {

//       if (!reschedulingId || !rescheduleDate) return;


 

//       // Find the doctor ID from the appointment we are rescheduling

//       const appointment = myList.find(a => a.id === reschedulingId);

//       const doctorId = appointment?.doctor?.id;


 

//       if (doctorId) {

//         try {

//           const response = await axiosInstance.get(

//             `/clinicq/patient/slots?doctorId=${doctorId}&date=${rescheduleDate}`

//           );

//           // SAVE TO THE NEW INDEPENDENT STATE

//           setRescheduleSlots(response.data);

//           // 3. Update the state. Do NOT filter here so we can show (Booked) labels

//           //setAvailableSlots(response.data);

//           // Only show slots that are NOT booked

//           //setAvailableSlots(response.data.filter((s: any) => !s.booked));

//         } catch (err) {

//           console.error("Error fetching reschedule slots:", err);

//           //setAvailableSlots([]);

//           // SAVE TO THE NEW INDEPENDENT STATE

//           setRescheduleSlots([]);

//         }

//       } else {

//         console.warn("No Doctor ID found for Appointment:", reschedulingId);

//       }

//     };


 

//     fetchRescheduleSlots();

//   }, [reschedulingId, rescheduleDate, myList]);



 

//   return (

//     <div className="d-flex bg-black text-white vh-100 overflow-hidden">


 

//       {/* SIDEBAR */}

//       <div className="bg-dark border-end border-secondary p-3 d-flex flex-column" style={{ width: '220px' }}>

//         <div className="d-flex align-items-center mb-4 mt-2 ps-2 text-info">

//           <div className="bg-info rounded-circle me-2" style={{ width: '22px', height: '22px' }}></div>

//           <h5 className="fw-bold mb-0 text-info">ClinicQ</h5>

//         </div>

//         <div className="nav flex-column gap-2">

//           <button onClick={() => onNavigate('book')} className="btn btn-info text-start py-2 px-3 rounded-3 fw-bold btn-sm shadow">📅 Book Appt</button>

//           <button onClick={() => onNavigate('my')} className="btn btn-outline-secondary text-white text-start py-2 px-3 rounded-3 border-0 btn-sm">📋 My Appts</button>

//           <button onClick={() => onNavigate('history')} className="btn btn-outline-secondary text-white text-start py-2 px-3 rounded-3 border-0 btn-sm">🕒 History</button>

//           <div className="border-top border-secondary mt-auto pt-2">

//             <button onClick={() => window.location.reload()} className="btn btn-outline-danger w-100 text-start py-2 px-3 rounded-3 border-0 btn-sm d-flex align-items-center">

//               <span className="me-2">📤</span> <span className="fw-bold">Log Out</span>

//             </button>

//           </div>

//         </div>

//       </div>


 

//       {/* MAIN CONTENT */}

//       <div className="flex-grow-1 p-3 h-100 overflow-hidden">

//         <div className="row g-3 h-100">


 

//           {/* LEFT COLUMN: BOOKING FLOW */}

//           <div className="col-xl-6 d-flex flex-column h-100">

//             <h4 className="fw-bold mb-2">Book Appointment</h4>


 

//             {/* CATEGORIES (Back at the top) */}

//             {/* --- UPDATED UI: CATEGORIES FLOW --- */}


 

//             <div className="d-flex gap-3 mb-4 mt-2 justify-content-between">

//               {specs.map(s => {

//                 // Determine if this specific category is currently selected

//                 const isSelected = selectedSpec === s.name;


 

//                 return (

//                   <button

//                     key={s.name}

//                     onClick={() => setSelectedSpec(s.name)}

//                     className="btn d-flex flex-column align-items-center justify-content-center flex-grow-1 transition-all"

//                     style={{

//                       maxWidth: '120px',

//                       height: '100px',

//                       // Darker base background, with a very subtle tint of the category color

//                       backgroundColor: isSelected ? `${s.color}15` : '#111',

//                       borderRadius: '16px',


 

//                       // Use the dynamic color for the border ONLY when selected

//                       border: `2px solid ${isSelected ? s.color : '#222'}`,


 

//                       // Add a subtle depth shadow and a colored 'glow' when selected

//                       boxShadow: isSelected

//                         ? `0 0 15px ${s.color}40, inset 0 0 5px ${s.color}30`

//                         : '0 4px 6px rgba(0,0,0,0.3)',


 

//                       // Ensure smooth transition when switching

//                       transition: 'all 0.2s ease-in-out',

//                       transform: isSelected ? 'scale(1.03)' : 'scale(1)'

//                     }}

//                   >

//                     {/* --- Icon with dynamic color tint --- */}

//                      <span

//                       className="fs-2 mb-1"

//                       style={{

//                         opacity: isSelected ? 1 : 0.6,

//                         filter: isSelected ? `drop-shadow(0 0 3px ${s.color})` : 'none'

//                       }}

//                     >

//                       {s.icon}

//                     </span>


 

//                     {/* <i

//                     className={`${s.icon} fs-2 mb-2`}

//                     style={{

//                       color:s.color,

//                       opacity:isSelected?1:0.6,

//                       filter: isSelected? `drop-shadow(0 0 8px ${s.color})`:'none',

//                       transition:'all 0.3s ease'

//                     }}></i> */}


 

//                     {/* --- Text Label --- */}

//                     <span

//                       className="fw-bold text-uppercase"

//                       style={{

//                         fontSize: '10px',

//                         letterSpacing: '1px',

//                         // Text is muted unless selected, then it uses the vibrant category color

//                         color: isSelected ? s.color : '#666',

//                         opacity: isSelected ? 1 : 0.7

//                       }}

//                     >

//                       {s.name}

//                     </span>

//                   </button>

//                 );

//               })}

//             </div>



 

//             <div className="card bg-dark border-0 rounded-4 p-3 flex-grow-1 shadow-lg d-flex flex-column justify-content-between"

//               style={{ maxHeight: 'calc(100vh - 250px)', minHeight: '500px' }}>

//               <div>

//                 {/* DOCTOR (LEFT) & LOCATION/DESC (RIGHT) */}

//                 <div className="row g-2 mb-3">

//                   <div className="col-6">

//                     <div className="p-2 rounded-3 bg-black border border-info h-100 d-flex align-items-center position-relative">

//                       <div className="flex-grow-1">

//                         <small className="text-info fw-bold" style={{ fontSize: '8px' }}>SELECT DOCTOR</small>


 

//                         <select

//                           className="form-select bg-transparent text-white border-0 p-0 shadow-none fw-bold"

//                           onChange={(e) => {

//                             const selected = doctors.find((doc) => doc.id === Number(e.target.value));

//                             setSelectedDoctor(selected || null);

//                           }}

//                           value={selectedDoctor?.id || ""}

//                           style={{ fontSize: '13px', appearance: 'none', cursor: 'pointer' }}

//                         >

//                           <option value="" className="bg-dark text-white">Select Dr.</option>


 

//                           {doctors.length > 0 ? (

//                             doctors.map((doc) => (

//                               <option

//                                 key={doc.id}

//                                 value={doc.id}

//                                 className="bg-dark text-white"

//                               >

//                                 {/* CRITICAL: Ensure this matches your DoctorDTO field name exactly */}

//                                 {doc.name || "Unknown Doctor"}

//                               </option>

//                             ))

//                           ) : (

//                             <option disabled className="bg-dark text-muted">No doctors found</option>

//                           )}

//                         </select>


 

//                       </div>

//                       <span className="text-info small pe-1">▼</span>

//                     </div>

//                   </div>

//                   <div className="col-6">


 

//                     <div className="p-3 rounded-3 bg-black border border-secondary h-100 d-flex flex-column justify-content-center shadow-sm" style={{ borderLeft: '4px solid #00f2fe' }}>


 

//                       {/* --- Location Title --- */}

//                       <div className="d-flex align-items-center mb-1">

//                         <span className="me-1" style={{ fontSize: '14px' }}>📍</span>

//                         <div className="text-info fw-bold text-uppercase" style={{ fontSize: '12px', letterSpacing: '0.5px' }}>

//                           {selectedDoctor?.location || "LOCATION"}

//                         </div>

//                       </div>


 

//                       {/* --- Description Text --- */}

//                       <div className="d-flex align-items-start mt-1">

//                         <span className="me-1" style={{ fontSize: '11px' }}>✨</span>

//                         <div style={{

//                           fontSize: '11px',

//                           lineHeight: '1.4',

//                           color: '#e0e0e0', // Much brighter light grey/white

//                           fontStyle: 'italic',

//                           textShadow: '0 0 8px rgba(0, 242, 254, 0.2)' // Very subtle cyan glow to make it "pop"

//                         }}>

//                           {selectedDoctor?.description ? `"${selectedDoctor.description}"` : "Consultation details available upon selection."}

//                         </div>

//                       </div>


 

//                     </div>

//                   </div>


 

//                 </div>


 

//                 {/* DATE (ABOVE TIME SLOTS) */}

//                 <div className="mb-3">

//                   <div className="p-2 rounded-3 bg-black border border-secondary d-flex align-items-center">

//                     <span className="me-2 text-info fs-6">📅</span>

//                     <input type="date" className="bg-transparent text-white border-0 p-0 fw-bold w-100 shadow-none" value={selectedDate} min={today} onChange={(e) => setSelectedDate(e.target.value)} style={{ colorScheme: 'dark', fontSize: '13px' }} />

//                   </div>

//                 </div>


 

//                 {/* TIME SLOTS */}

//                 <p className="text-secondary fw-bold text-uppercase mb-2" style={{ fontSize: '10px' }}>Available Slots</p>


 

//                 {/* UPDATED WRAPPER */}

//                 <div style={{

//                   flex: '1 1 auto',       // This tells the div to take up available space

//                   maxHeight: '220px',     // Keeps it from getting too big

//                   overflowY: 'auto',      // Ensure vertical scrolling

//                   overflowX: 'hidden',    // Prevent horizontal shifting

//                   paddingRight: '10px',   // Space for the scrollbar

//                   marginBottom: '20px'    // Gap before the button

//                 }}

//                   className="custom-scrollbar"

//                 >

//                   <div className="d-flex flex-wrap gap-2 pb-2">

//                     {availableSlots.length > 0 ? (

//                       availableSlots.map((slot: any) => (

//                         <button

//                           key={slot.id}

//                           onClick={() => !slot.booked && setActiveSlot(slot.startTime)}

//                           className={`btn rounded-pill px-3 py-1 btn-sm ${activeSlot === slot.startTime ? 'btn-info text-black shadow-glow' : 'btn-outline-info'}`}

//                           style={{

//                             // Add visual feedback so you know if it's disabled

//                             opacity: slot.booked ? 0.3 : 1,

//                             cursor: slot.booked ? 'not-allowed' : 'pointer',

//                             minWidth: '70px' // Keeps buttons uniform

//                           }}

//                           disabled={slot.booked}

//                         >

//                           {slot.startTime.substring(0, 5)}

//                         </button>

//                       ))

//                     ) : (

//                       <small className="text-secondary">No slots available for this date.</small>

//                     )}

//                   </div>

//                 </div>



 

//               </div>


 

//               {/* CONFIRM BUTTON - Now stays at the bottom */}

//               <div className="border-top border-secondary pt-3 mt-auto">

//                 <button onClick={handleBooking} className="btn btn-info w-100 py-2 rounded-pill fw-bold fs-6 text-black shadow">

//                   CONFIRM & BOOK

//                 </button>

//               </div>

//             </div>

//           </div>


 

//           {/* RIGHT COLUMN: MY LIST & RESCHEDULE FORM */}

//           <div className="col-xl-6 d-flex flex-column h-100">

//             <h4 className="fw-bold mb-2">My List</h4>


 

//             {/* THE LIST */}

//             <div className="card bg-dark border-0 rounded-4 p-3 mb-2 shadow-lg flex-grow-1 overflow-auto" style={{ backgroundColor: '#111' }}>


 

//               {myList.map((app: any) => (


 

//                 <div key={app.id} className={`mb-3 pb-2 border-bottom border-secondary last-border-0 transition-all ${reschedulingId === app.id ? 'bg-info bg-opacity-10 p-2 rounded' : ''}`}>

//                   <div className="d-flex justify-content-between">

//                     <span className="text-info fw-bold">{app.doctor?.name || "Doctor Name"}</span>

//                     <span className={`badge bg-${app.color}-subtle text-${app.color} border border-${app.color} rounded-pill`} style={{ fontSize: '9px' }}>{app.status}</span>


 

//                   </div>



 

//                   <div className="text-secondary mb-2 small">{app.timeSlot?.slotDate} | {app.timeSlot?.startTime?.substring(0, 5)}</div>

//                   {app.status === 'BOOKED' && (

//                     <div className="d-flex gap-2">

//                       <button className="btn btn-warning btn-sm py-1 px-3 rounded-pill fw-bold text-black shadow-sm" onClick={() => handleRescheduleClick(app)}>

//                         Reschedule


 

//                       </button>

//                       <button className="btn btn-outline-danger btn-sm py-1 px-3 rounded-pill fw-bold" onClick={() => handleCancel(app.id)}>Cancel</button>

//                     </div>

//                   )}

//                 </div>

//               ))}

//             </div>


 

//             {/* US 03: RESCHEDULE FORM (Enabled on click) */}

//             <div className={`card border-0 rounded-4 p-3 shadow-lg transition-all ${reschedulingId ? 'bg-black border border-warning' : 'bg-dark opacity-25'}`}>

//               <h6 className="text-warning fw-bold mb-3" style={{ fontSize: '12px' }}>{reschedulingId ? 'MODIFY APPOINTMENT' : 'SELECT ITEM TO RESCHEDULE'}</h6>

//               <div className="row g-2">

//                 <div className="col-6">


 

//                   <input type="date" className="form-control form-control-sm bg-dark text-white border-secondary shadow-none"  value={rescheduleDate} min={today} onChange={(e) => setRescheduleDate(e.target.value)} style={{ colorScheme: 'dark' }} />

//                 </div>

//                 <div className="col-6">

//                   <select

//                     className="form-select form-select-sm bg-dark text-white border-secondary shadow-none"

//                     disabled={!reschedulingId}

//                     value={rescheduleTime}

//                     onChange={(e) => setRescheduleTime(e.target.value)}

//                   >

//                     <option value="">New Time</option>

//                     {rescheduleSlots && rescheduleSlots.length > 0 ? (

//                       rescheduleSlots.map((slot: any) => (

//                         <option

//                           key={slot.id}

//                           value={slot.id}

//                           // This disables the option if booked is true

//                           disabled={slot.booked}

//                           style={{ color: slot.booked ? '#666' : '#fff' }}

//                         >

//                           {/* This adds the text (Booked) dynamically */}

//                           {slot.startTime.substring(0, 5)} {slot.booked ? '(BOOKED)' : ''}

//                         </option>

//                       ))

//                     ) : (

//                       <option disabled>No slots found</option>

//                     )}

//                   </select>

//                 </div>


 

//               </div>

//               <button className={`btn btn-warning w-100 mt-3 py-2 rounded-pill fw-bold text-black ${!reschedulingId ? 'disabled' : ''}`} onClick={handleApplyReschedule}> APPLY CHANGES </button>

//             </div>


 

//           </div>

//         </div>

//       </div>

//     </div>

//   );

// };


 

// export default BookAppointment;



 

import React, { useEffect, useState, useCallback } from 'react';

import { FaHospitalUser, FaStethoscope, FaHeart, FaBone } from 'react-icons/fa6';

import Swal from 'sweetalert2';

import axiosInstance from '../../../services/axiosInstance';

import axios from 'axios';

import { useNavigate } from 'react-router-dom';

interface BookProps {

  onNavigate: (tab: 'book' | 'my' | 'history'  | 'token') => void;

}

type Doctor = {

  id: number;

  name: string;

  location: string;

  description: string;

  days?: string[];

};


 

const BookAppointment: React.FC<BookProps> = ({ onNavigate }) => {

  const navigate=useNavigate();

  const [selectedSpec, setSelectedSpec] = useState("GENERAL");

  const [availableSlots, setAvailableSlots] = useState<any[]>([]);

  const [activeSlot, setActiveSlot] = useState<string | null>(null);

  const today = new Date().toLocaleDateString('en-CA');

  const [selectedDate, setSelectedDate] = useState(today);

  const [doctors, setDoctors] = useState<Doctor[]>([]);

  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);

  const [reschedulingId, setReschedulingId] = useState<number | null>(null);

  const [rescheduleDate, setRescheduleDate] = useState("");

  const [rescheduleTime, setRescheduleTime] = useState("");

  const [rescheduleSlots, setRescheduleSlots] = useState<any[]>([]);

  const [rescheduleDoctor, setRescheduleDoctor] = useState<Doctor | null>(null);

  const [myList, setMyList] = useState<any[]>([]);

  const [maxDaysRule, setMaxDaysRule] = useState<number | null>(null);

  const [rescheduleCutoff,setRescheduleCutoff]=useState<number>(1);

  const formatMinutes=(totalMinutes:number)=>{

    const hours=Math.floor(totalMinutes/60);

    const minutes=totalMinutes%60;

    if(hours>0){

      return `${hours} hour${hours>1?'s':''} and ${minutes} minute${minutes !== 1?'s':''}`;

    }

    return `${minutes} minute${minutes!==1?'s':''}`;

  };

  useEffect(()=>{

    const fetchMaxDays=async()=>{

      try{

        const response=await axiosInstance.get('/clinicq/patient/appointments/rules');

        setMaxDaysRule(response.data.maxDaysInAdvance || 15);

        setRescheduleCutoff(response.data.cancellationCutoffHours|| 1);

      }catch(err){

        console.error("Error fetching rules:",err);

      }

    };

    fetchMaxDays();

  },[]);


 

  const specs = [

    { name: "GENERAL", icon: <FaHospitalUser />, color: "#ff7e5f" },

    { name: "CARDIOLOGY", icon: <FaHeart />, color: "#ff75c3" },

    { name: "ORTHOPEDICS", icon: <FaBone />, color: "#ffa64d" },

    { name: "PEDIATRICS", icon: <FaStethoscope />, color: "#20c997" }


 

  ];

  // --- LOGIC (STRICTLY UNTOUCHED) ---


 

  const fetchUpcomingAppointments = useCallback(() => {

    axiosInstance.get(`/clinicq/patient/appointments/upcoming`)

      .then(res => setMyList(res.data))

      .catch(err => console.error("Error fetching appointments:", err));


 

  }, []);


 

  const fetchSlots = useCallback(() => {

    const currentLimit = maxDaysRule || 15;

    if (selectedDoctor && selectedDate) {

      //const today=new Date().toISOString().split('T')[0];

      if(selectedDate<today){

        setAvailableSlots([]);

        return;

      }

     

      const limitDate = new Date();

      limitDate.setDate(new Date().getDate() + currentLimit);

      const maxDateString = limitDate.toLocaleDateString('en-CA');

      if (selectedDate >maxDateString) {

        setAvailableSlots([]);

        return;

      }

      axiosInstance.get(`/clinicq/patient/slots`, {

        params: { doctorId: selectedDoctor.id, date: selectedDate },

      })

        .then((res) => setAvailableSlots(res.data))

        .catch(err => {

          setAvailableSlots([]);

          console.error(err);


 

        });

    }


 

  }, [selectedDoctor, selectedDate, today,maxDaysRule]);


 

  useEffect(() => { fetchUpcomingAppointments(); }, [fetchUpcomingAppointments]);

  useEffect(() => {

    const fetchDoctors = async () => {

      try {

        const response = await axiosInstance.get(`/clinicq/patient/doctors`, {

          params: { department: selectedSpec }

        });

        // starting from here

        const basicDoctors = response.data;

        const schedRes = await axiosInstance.get('/clinicq/admin/doctors');

        const doctorsWithDays = basicDoctors.map((doc: any) => {

          const doctorSchedules = schedRes.data.filter((s: any) => s.doctor.id === doc.id);

          const days = doctorSchedules.map((s: any) => s.dayOfWeek);

          return { ...doc, days };

        });

        setDoctors(doctorsWithDays);

        setSelectedDoctor(null);

      } catch (err) { setDoctors([]); }


 

    };

    if (selectedSpec) fetchDoctors();

  }, [selectedSpec]);


 

  useEffect(() => { fetchSlots(); }, [fetchSlots]);



 

  const handleBooking = async () => {

    if (!selectedDoctor) {

      Swal.fire({ title: 'Doctor Not Selected', text: 'Please choose a doctor.', icon: 'warning', confirmButtonColor: '#ff7e5f' });

      return;

    }

    if (!activeSlot) {

      Swal.fire({ title: 'Select a Time', text: 'Please pick a slot.', icon: 'info', confirmButtonColor: '#ff7e5f' });

      return;

    }

    const selectedSlot = availableSlots.find(s => s.startTime === activeSlot);

    if (!selectedSlot) return;

    const bookingData = { patientId: 1, doctorId: selectedDoctor.id, slotId: selectedSlot?.id, reason: "General Consultation" };

    try {


 

      const response = await axiosInstance.post("/clinicq/patient/appointment", bookingData);

      if (response.status === 201) {

        Swal.fire({ title: 'Appointment Booked!', icon: 'success', confirmButtonColor: '#ff7e5f' });

        fetchUpcomingAppointments();

        setActiveSlot(null);

        fetchSlots();

      }

    } catch (err: any) {

      Swal.fire({ title: 'Booking Failed', text: err.response?.data?.errorMessage, icon: 'error' });

    }

  };


 

  const handleRescheduleClick = async (app: any) => {

    const appointmentDateTime=new Date(`${app.timeSlot.slotDate}T${app.timeSlot.startTime}`);

    const now=new Date();

    const diffInMinutes=Math.floor((appointmentDateTime.getTime()-now.getTime())/(1000*60));

    const cutoffMinutes=(rescheduleCutoff||1)*60;

    if(diffInMinutes<cutoffMinutes){

      const minutesLate=cutoffMinutes-diffInMinutes;

      const formattedLateTime=formatMinutes(minutesLate);

      Swal.fire({

        icon:'error',

        title:'<span style="color:#d33; font-weight:700;">Reschedule Window Closed</span>',

        html:`<div style="font-family:'Segoe UI',sans-serif;color:#444;line-height:1.4;">

          <div style="background:#fff5f5; border-radius:12px; padding:12px;border-left:5px solid #ff4d4d;margin-bottom:10px;">

          <p style="margin: 0;font-weight:600;font-size:14px;">Clinic Policy:</p>

          <p style="margin:0;font-size:12px;">Rescheduling must be done at least <b>${rescheduleCutoff} hours</b> before the appointment.</p>

          </div>

          <p style="font-size:14px;margin-bottom:5px;">You missed the deadline by</p>

          <p style="color:#ff4d4d; font-size:18px; font-weight:700;margin:0;">${formattedLateTime}</p>

         

          </div>

       

        `,

        showConfirmButton:true,

        confirmButtonText:'Got it',

        confirmButtonColor:'#ff7e5f',

        background:'#ffffff',

       

        showClass:{popup:'animate__animated animate__fadeInDown'}

      });

      return;

    }

    setReschedulingId(app.id);

    const fullDoctorInfo = doctors.find(d => d.id === app.doctor.id);

    const docToUse = fullDoctorInfo || app.doctor;

    setRescheduleDoctor(docToUse);

    setRescheduleDate(app.timeSlot?.slotDate);

    setRescheduleTime("");

    const dayName = new Date(app.timeSlot?.slotDate).toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase();

    const workingDays = docToUse.days || [];


 

    if (workingDays.includes(dayName)) {

      try {

        const response = await axiosInstance.get(`/clinicq/patient/slots?doctorId=${app.doctor?.id}&date=${app.timeSlot?.slotDate}`);

        setRescheduleSlots(response.data);

      } catch (err) { setRescheduleSlots([]); }


 

    }else{

      setRescheduleSlots([]);

    }


 

  };


 

  const handleApplyReschedule = async () => {

    if (!reschedulingId || !rescheduleTime) return;

    try {

      const response = await axiosInstance.put(`/clinicq/patient/appointments/reschedule`, null, {

        params: { appointmentId: reschedulingId, newSlotId: rescheduleTime }

      });

      if (response.status === 200) {

        Swal.fire({ title: 'Updated!', text: 'Rescheduled successfully.', icon: 'success', confirmButtonColor: '#ff7e5f' });

        setReschedulingId(null);

        fetchUpcomingAppointments();

        fetchSlots();

      }


 

    } catch (err: any) {

      Swal.fire({ icon: 'error', title: 'Failed', text: err.response?.data?.errorMessage });

    }


 

  };


 

  const handleCancel = async (id: number) => {

    const result = await Swal.fire({ title: 'Are you sure?', text: "Cancel this appointment?", icon: 'warning', showCancelButton: true, confirmButtonColor: '#d33' });

    if (result.isConfirmed) {

      try {

        await axiosInstance.delete(`/clinicq/patient/appointments/cancel/${id}`);

        setMyList(prev => prev.filter(app => app.id !== id));

        fetchSlots();

        Swal.fire('Cancelled!', 'Slot is now free.', 'success');


 

      } catch (err) { Swal.fire('Action Failed', 'Cancellation unavailable in this timeframe', 'error'); }

    }

  };

  const handleCancelReschedule = () => {

    setReschedulingId(null);

    setRescheduleDate("");

    setRescheduleTime("");

    setRescheduleSlots([]);

    setRescheduleDoctor(null);

  };

  // Calculate the maximum allowed date based on the dynamic rule



 

  const handleDateSelection = (e: React.ChangeEvent<HTMLInputElement>) => {

    if (!e || !e.target) return;

    const chosenDate = e.target.value;

    if (!chosenDate) return;

    if (chosenDate < today) {

      setAvailableSlots([]);

      Swal.fire({

        title: 'Invalid Date',

        text: "You cannot book appointments for a past date",

        icon: 'error',

        confirmButtonColor: '#ff7e5f'

      });

      return;

    }


 

    const limit = maxDaysRule || 15;

    const limitDate = new Date();

    limitDate.setDate(new Date().getDate() + limit);

    const maxDateString = limitDate.toLocaleDateString('en-CA');


 

    if (chosenDate > maxDateString) {

      setAvailableSlots([]);

      Swal.fire({

        title: 'Booking Policy',

        text: `According to current clinic rules, appointments can only be booked up to ${limit} days in advance.`,

        icon: 'warning',

        confirmButtonColor: '#ff7e5f'

      });

      return; // Stop the update

    }

    if(selectedDoctor && selectedDoctor.days){

      const dayName = new Date(chosenDate).toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase();

      const workingDays = selectedDoctor.days || [];

      const isAvailable = workingDays.some(d => d.toUpperCase() === dayName);

      if (!isAvailable) {

        setAvailableSlots([]);

        Swal.fire({

          title: 'Doctor Unavailable',

          text: `${selectedDoctor.name} is only available on: ${workingDays.join(", ")}`,

          icon: 'info',

          confirmButtonColor: '#ff7e5f'

        });

        return;

      }

    }


 

    setSelectedDate(chosenDate);

  };


 

  const handleRescheduleDateChange = (dateString: string) => {

    if (!dateString || !rescheduleDoctor) return;

    setRescheduleDate(dateString);

    if (dateString < today) {

      setRescheduleSlots([]);

      Swal.fire({

        title: 'Invalid Date',

        text: "You cannot reschedule to a past date",

        icon: 'error',

        confirmButtonColor: '#ff7e5f'

      });

      return;

    }

    const limit = maxDaysRule || 15;

    const limitDate = new Date();

    limitDate.setDate(new Date().getDate() + limit);

    const maxDateString = limitDate.toLocaleDateString('en-CA');


 

    if (dateString > maxDateString) {

      setRescheduleSlots([]);

      Swal.fire({

        title: 'Reschedule Limit',

        text: `You can only reschedule within the next ${limit} days.`,

        icon: 'warning',

        confirmButtonColor: '#ff7e5f'

      });

      return; // Stop the update

    }


 

    // update

    const dayName = new Date(dateString).toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase();

    const workingDays = rescheduleDoctor.days || [];

    const isAvailable = workingDays.some(d => d.toUpperCase() === dayName);

    if (!isAvailable) {

      setRescheduleSlots([]);

      Swal.fire({

        title: 'Doctor Unavailable',

        text: `${rescheduleDoctor.name} is only available for rescheduling on: ${workingDays.join(", ")}`,

        icon: 'info',

        confirmButtonColor: '#ff7e5f'

      });

      return;

    }

    axiosInstance.get(`/clinicq/patient/slots`,{

      params:{doctorId:rescheduleDoctor.id,date:dateString}

    })

    .then(res=>{

      setRescheduleSlots(res.data);

    })

    .catch((err)=>{

      setRescheduleSlots([]);

    });

    setRescheduleDate(dateString);

  };


 

  return (

    <div className="d-flex min-vh-100 overflow-hidden" style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>

      <style>


 

        {`


 

          .glass-card-light { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(25px); -webkit-backdrop-filter: blur(25px); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); }


 

          .custom-scrollbar::-webkit-scrollbar { width: 6px; }


 

          .custom-scrollbar::-webkit-scrollbar-thumb { background: #dee2e6; border-radius: 10px; }


 

          .category-btn { transition: all 0.3s ease; border-radius: 20px; }


 

          .category-btn:hover { transform: translateY(-3px); }


 

          .slot-btn { transition: all 0.2s ease; border-radius: 12px !important; font-weight: 600; min-width: 85px; }


 

          .logout-btn{

            transition: all 0.2s ease;

            border-radius:12px !important;

            background:transparent;

          }

          .logout-btn:hover{

            background:rgba(220,53,69,0.1) !important;

            color: #dc3545 !important;

          }

          .logout-btn:active{

            background: rgba(220,53,69,0.2) !important ;

            transform:scale(0.96);

          }

          .swal2-actions{

            margin-top:0.5rem !important;

          }

          .swal2-html-container{

            margin-bottom:0px !important;

          }


 

        `}

      </style>


 

      {/* SIDEBAR */}


 

      <div className="bg-white border-end p-3 d-flex flex-column shadow-sm" style={{ width: '240px' }}>

        <div className="d-flex align-items-center mb-4 mt-2 ps-2">

          <div className="rounded-circle me-2 d-flex align-items-center justify-content-center fw-bold text-white"

            style={{ width: '35px', height: '35px', background: '#ff7e5f' }}>🏠</div>

          <h4 className="fw-bold mb-0" style={{ color: '#111' }}>ClinicQ</h4>

        </div>


 

        <div className="nav flex-column gap-2">

          <button onClick={() => onNavigate('book')} className="btn text-start py-2 px-3 border-0 fw-bold" style={{ background: 'rgba(255, 126, 95, 0.1)', color: '#ff7e5f', borderRadius: '12px' }}>📅 Book Appointment</button>

          <button onClick={() => onNavigate('my')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">📋 My Appointments</button>

          <button onClick={() => onNavigate('history')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">🕒 History</button>

          <button onClick={() => onNavigate('token')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">🎟️ Token No</button>

          <div className="mt-auto pt-4 border-top">

            <button onClick={() =>{ localStorage.removeItem("token"); navigate('/');}} className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-2

            style={{fontSize:'13px'}}"><span>📤</span> Log Out</button>

          </div>

        </div>

      </div>


 

      {/* MAIN CONTENT */}

      <div className="flex-grow-1 p-4 h-100 overflow-hidden">

        <div className="row g-4 h-100">

          {/* LEFT: BOOKING */}

          <div className="col-xl-6 d-flex flex-column h-100">

            <h3 className="fw-bold mb-4">Schedule a Visit</h3>

            <div className="d-flex gap-3 mb-4">

              {specs.map(s => (

                <button key={s.name} onClick={() => setSelectedSpec(s.name)} className="btn category-btn flex-grow-1 d-flex flex-column align-items-center justify-content-center p-3"

                  style={{

                    border: `2px solid ${selectedSpec === s.name ? s.color : '#eee'}`,

                    background: selectedSpec === s.name ? `${s.color}15` : '#fff',

                  }}>

                  <span className="fs-3 mb-1" style={{ color: s.color }}>{s.icon}</span>

                  <span className="fw-bold text-uppercase" style={{ fontSize: '9px', color: selectedSpec === s.name ? s.color : '#6c757d' }}>{s.name}</span>

                </button>


 

              ))}

            </div>

            <div className="glass-card-light p-4 flex-grow-1 d-flex flex-column shadow-lg">

              <div className="row g-3 mb-4">

                <div className="col-6">

                  <label className="small fw-bold text-muted mb-1 d-block">DOCTOR</label>

                  <select className="form-select border-0 shadow-sm rounded-3 fw-bold bg-light" value={selectedDoctor?.id || ""}

                    onChange={(e) => setSelectedDoctor(doctors.find(d => d.id === Number(e.target.value)) || null)}>

                    <option value="">Select Professional</option>

                    {doctors.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}

                  </select>

                </div>


 

                <div className="col-6">

                  <label className="small fw-bold text-muted mb-1 d-block">DATE</label>

                  <input type="date"

                    className="form-control border-0 shadow-sm rounded-3 fw-bold bg-light"

                    value={selectedDate}

                    min={today}

                    max={(() => {

                      const d = new Date();

                      d.setDate(d.getDate() + (maxDaysRule || 15));

                      return d.toLocaleDateString('en-CA');

                    })()}

                    onChange={handleDateSelection} />

                </div>

              </div>


 

              {selectedDoctor && (

                <div className="p-3 mb-4 rounded-4" style={{ background: 'rgba(255, 126, 95, 0.05)', borderLeft: '4px solid #ff7e5f' }}>

                  <small className="fw-bold d-block text-uppercase" style={{ color: '#ff7e5f', fontSize: '10px' }}>{selectedDoctor.location}</small>

                  <p className="small mb-0 mt-1 italic text-muted">"{selectedDoctor.description}"</p>

                </div>


 

              )}

              <label className="small fw-bold text-muted mb-2 d-block">TIME SLOT</label>

              <div className="custom-scrollbar overflow-auto flex-grow-1 mb-3">

                <div className="d-flex flex-wrap gap-2">

                  {availableSlots.length > 0 ? availableSlots

                    .filter(slot => {

                      if (selectedDate < today) return false;

                      if (selectedDate === today) {

                        const currentTime = new Date().toTimeString().substring(0, 5);

                        return slot.startTime > currentTime;

                      }

                      return true;

                    })

                    .map(slot => (

                      <button key={slot.id} disabled={slot.booked} onClick={() => setActiveSlot(slot.startTime)}

                        className={`btn btn-sm slot-btn ${activeSlot === slot.startTime ? 'text-white' : 'btn-outline-secondary'}`}

                        style={{ opacity: slot.booked ? 0.4 : 1, background: activeSlot === slot.startTime ? '#ff7e5f' : '', borderColor: activeSlot === slot.startTime ? '#ff7e5f' : '' }}>

                        {slot.startTime.substring(0, 5)}

                      </button>

                    )) : <p className="text-muted small">No slots found.</p>}

                  {/* {availableSlots.length>0 && availableSlots.filter(s=>selectedDate>=today).length===0 && (

                      <p className='text-muted small'>No slots found for this date.</p>

                    )} */}

                </div>

              </div>

              <button onClick={handleBooking} className="btn w-100 py-3 rounded-pill fw-bold text-white shadow-lg mt-auto" style={{ background: 'linear-gradient(90deg, #ff7e5f, #ff6b6b)' }}>

                CONFIRM & BOOK

              </button>

            </div>

          </div>

          {/* RIGHT: LIST & RESCHEDULE */}

          <div className="col-xl-6 d-flex flex-column h-100">

            <h3 className="fw-bold mb-4">Patient Dashboard</h3>

            <div className="glass-card-light p-4 flex-grow-1 overflow-auto custom-scrollbar mb-4">

              <h6 className="fw-bold text-muted small text-uppercase mb-3">Upcoming</h6>

              {myList.map(app => (

                <div key={app.id} className="p-3 mb-3 rounded-4 bg-white border" style={{ border: reschedulingId === app.id ? '2px solid #ff7e5f' : '1px solid #eee' }}>

                  <div className="d-flex justify-content-between align-items-center">

                    <span className="fw-bold">{app.doctor?.name}</span>

                    <span className="badge rounded-pill bg-success-subtle text-success px-3">{app.status}</span>

                  </div>

                  <div className="small text-muted mt-1">{app.timeSlot?.slotDate} | {app.timeSlot?.startTime?.substring(0, 5)}</div>

                  {app.status === 'BOOKED' && (

                    <div className="mt-3 d-flex gap-2">

                      <button className="btn btn-sm px-3 rounded-pill fw-bold bg-warning-subtle text-warning-emphasis border-0" onClick={() => handleRescheduleClick(app)}>Reschedule</button>

                      <button className="btn btn-sm px-3 rounded-pill fw-bold bg-danger-subtle text-danger border-0" onClick={() => handleCancel(app.id)}>Cancel</button>

                    </div>

                  )}

                </div>

              ))}

            </div>

            <div className={`glass-card-light p-4 ${reschedulingId ? 'shadow-lg border-warning' : 'opacity-50'}`} style={{ border: reschedulingId ? '2px solid #ffc107' : '' }}>

              <h6 className="fw-bold mb-3 small text-warning text-uppercase">Reschedule Protocol</h6>

              <div className="row g-2">

                <div className="col-6">

                  <input type="date" className="form-control rounded-3"

                    value={rescheduleDate}

                    min={today}

                    max={maxDaysRule ? (() => {

                      const d = new Date();

                      d.setDate(d.getDate() + maxDaysRule);

                      return d.toISOString().split('T')[0];

                    })() : undefined}

                    onChange={(e) => handleRescheduleDateChange(e.target.value)} /></div>

                <div className="col-6">

                  <select className="form-select rounded-3" disabled={!reschedulingId || rescheduleSlots.length===0} value={rescheduleTime} onChange={(e) => setRescheduleTime(e.target.value)}>

                    <option value="">New Time</option>

                    {/*{rescheduleSlots.map(s => <option key={s.id} value={s.id} disabled={s.booked}>{s.startTime.substring(0, 5)} {s.booked ? '(Booked)' : ''}</option>)}*/}

                    {rescheduleSlots.length>0?(rescheduleSlots.filter(slot => {

                      if(rescheduleDate===today){

                        const currentTime = new Date().toTimeString().substring(0, 5);

                        return slot.startTime > currentTime;

                      }

                      return true;

                      // else if (rescheduleDate > today) return true;

                      // const currentTime = new Date().toTimeString().substring(0, 5);

                      // return slot.startTime > currentTime;

                    })

                      .map(s => (

                        <option key={s.id} value={s.id} disabled={s.booked}>

                          {s.startTime.substring(0, 5)}{s.booked ? '(Booked)' : ''}

                        </option>

                      ))

                    ):(

                      <option disabled>No Slots available</option>

                    )}

                  </select>

                </div>

              </div>

              <button disabled={!reschedulingId} onClick={handleApplyReschedule} className="btn w-100 mt-3 py-2 rounded-pill fw-bold text-white" style={{ background: reschedulingId ? '#ffc107' : '#dee2e6' }}>APPLY NEW TIME</button>

              <button

                type='button'

                className='btn w-100 mt-3 py-2 rounded-pill fw-bold text-white'

                style={{ background: reschedulingId ? '#d23105' : '#dee2e6', borderRadius: '12px' }}

                onClick={handleCancelReschedule}>CANCEL</button>

            </div>

          </div>

        </div>

      </div>

    </div>

  );

};


 

export default BookAppointment;




 