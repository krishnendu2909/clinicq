// import React, { useState, useEffect } from 'react';

// import axios from 'axios';

// import NowServing from './NowServing';

// import Swal from 'sweetalert2';

// import axiosInstance from '../../../services/axiosInstance';


 

// interface Token {

//   id: number;

//   tokenNumber: number;

//   status: string;

//   checkInTime: string;


 

//   patient: {

//     id: number;

//     name: string;

//     phone: string;

//     dateOfBirth: string;

//   };

//   doctor: {

//     id: number;

//     name: string;

//   };

//   appointment?: {

//     type: string;

//   };

// }


 

// interface Appointment {

//   id: number;

//   patientName: string;

//   age: number;

//   contact: string;

//   doctorId: string;

//   doctorName: string;

//   appointmentTime: string;

//   date: string;

//   status: 'Scheduled' | 'Checked-In' | 'Completed' | 'Cancelled';

//   reason?: string;

//   queueNumber?: string;

// }


 

// export interface Patient {

//   id: any;

//   name: string;

//   age: number;

//   phone?:string;

//   contact?: string;

//   dateOfBirth?:string;

//   reason: string;

//   queueNumber: string;

//   doctor?: string;

//   timestamp: Date;

//   type: 'walk-in' | 'appointment';

//   appointmentId?: string;

//   status: 'WAITING' | 'IN_CONSULTATION' | 'COMPLETED' | 'NO_SHOW';

//   position: number;

// }


 

// export interface Doctor {

//   id: string;

//   name: string;

//   specialization?: string; // Keep this if you use it locally

//   department: string;     // ADD THIS LINE

// }

// interface FormData {

//   name: string;

//   dob: string;

//   gender: string;

//   contact: string;

//   email: string;

//   password: string;

//   doctor: string;

//   reason?: string; // Adding the '?' makes this field optional

// }


 

// const ReceptionistDashboard: React.FC = () => {

//   const [patients, setPatients] = useState<any[]>([]);

//   const [appointments, setAppointments] = useState<Appointment[]>([]);

//   const [activeTab, setActiveTab] = useState<'walk-in' | 'appointments' | 'queue-management' | 'now-serving' | 'search'>('walk-in');

//   const [queueCounter, setQueueCounter] = useState<{ [key: string]: number }>({});

//   const [searchQuery, setSearchQuery] = useState('');

//   //const [searchResults, setSearchResults] = useState<Patient[]>([]);

//   const [showSearchResults, setShowSearchResults] = useState(false);

//   const DEPARTMENTS = ["GENERAL", "CARDIOLOGY", "ORTHOPEDICS", "PEDIATRICS"];

//   const [selectedDept, setSelectedDept] = useState<string>('');

//   const [departments, setDepartments] = useState<string[]>([]);

//   const [searchKeyword, setSearchKeyword] = useState('');

//   const [searchResults, setSearchResults] = useState<any[]>([]);

//   const [isSearching, setIsSearching] = useState(false);

//   const [filteredQueue, setFilteredQueue] = useState<any[]>([]);

//   // doctors state should already exist from previous steps

//   const [formData, setFormData] = useState<{

//     name: string;

//     dob: string;

//     gender: string;

//     contact: string;

//     email: string;

//     password: string;

//     doctor: string;

//     reason?: string; // Optional here too

//   }>({

//     name: '',

//     dob: '',

//     gender: 'MALE',

//     contact: '',

//     email: '',

//     password: '',

//     doctor: '',

//     reason: '' // You can keep this as an empty string default

//   });

//   const [doctors, setDoctors] = useState<Doctor[]>([]);


 

//   useEffect(() => {

//     const fetchTodayData = async () => {

//       try {

//         const response = await axiosInstance.get('/clinicq/receptionist/today');


 

//         const mapped = response.data.map((appt: any) => ({

//           id: appt.id, // Database Appointment ID

//           patientName: appt.patient.name,

//           age: 25, // Calculate from appt.patient.dateOfBirth if available

//           contact: appt.patient.phone,

//           doctorId: appt.doctor.id,

//           doctorName: appt.doctor.name,

//           appointmentTime: appt.timeSlot.startTime,

//           date: appt.timeSlot.slotDate,

//           status: appt.status, // Should be 'BOOKED'

//           reason: appt.reason,

//           type: appt.type // 'PRE_BOOKED' or 'WALK_IN'

//         }));

//         setAppointments(mapped);

//       } catch (err) {

//         console.error("Error fetching today's schedule:", err);

//       }

//     };

//     fetchTodayData();

//   }, []);


 

//   const generateQueueNumber = (doctorId: string): string => {

//     const currentCount = queueCounter[doctorId] || 0;

//     const newCount = currentCount + 1;

//     setQueueCounter(prev => ({ ...prev, [doctorId]: newCount }));

//     return `${doctorId === 'general' ? 'G' : 'D'}${newCount.toString().padStart(3, '0')}`;

//   };


 

//   const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {

//     const { name, value } = e.target;

//     setFormData(prev => ({

//       ...prev,

//       [name]: value

//     }));

//   };


 

//   const handleSubmit = async (e: React.FormEvent) => {

//     e.preventDefault();


 

//     // 1. Password Check: Your DTO requires min 6 characters

//     if (formData.password.length < 6) {

//       alert("Password must be at least 6 characters long.");

//       return;

//     }


 

//     // 2. Date Formatting: Ensure YYYY-MM-DD

//     let formattedDOB = formData.dob;

//     if (formData.dob.includes('-')) {

//       const parts = formData.dob.split('-');

//       // If it's DD-MM-YYYY, flip it. If it's YYYY-MM-DD, keep it.

//       if (parts[0].length === 2) {

//         formattedDOB = `${parts[2]}-${parts[1]}-${parts[0]}`;

//       }

//     }


 

//     const patientDTO = {

//       name: formData.name,

//       dateOfBirth: formattedDOB,

//       gender: formData.gender, // Must be "MALE" or "FEMALE"

//       phone: formData.contact,

//       user: {

//         email: formData.email,

//         password: formData.password,

//         role: "PATIENT"

//       }

//     };


 

//     try {

//       const response = await axiosInstance.post(

//         `/clinicq/receptionist/walkin/${formData.doctor}`,

//         patientDTO

//       );


 

//       const tokenData = response.data;

//       //update

//       console.log("token received: ", tokenData);

//       //const token=response.data.token;

//       if (tokenData.token) {

//         localStorage.setItem('token', tokenData.token);

//         axios.defaults.headers.common['Authorization'] = `Bearer ${tokenData.token}`; //axiosInstance or axios

//       }

//       const tokenNumber = tokenData.tokenNumber;

//       // --- SWEETALERT SUCCESS POPUP ---

//       Swal.fire({

//         title: 'Token Generated!',

//         html: `

//         <div style="text-align: left; padding: 10px;">

//           <p><b>Patient:</b> ${tokenData.patient.name}</p>

//           <p><b>Token Number:</b> <span style="color: #00f2fe; font-size: 1.5rem;">${tokenData.tokenNumber}</span></p>

//           <p><b>Doctor:</b> ${tokenData.doctor.name}</p>

//           <p><b>Status:</b> ${tokenData.status}</p>

//         </div>

//       `,

//         icon: 'success',

//         background: '#1a1d21', // Matching your dark theme

//         color: '#fff',

//         confirmButtonColor: '#00f2fe',

//         confirmButtonText: 'Great!',

//         showClass: {

//           popup: 'animate__animated animate__fadeInDown'

//         }

//       });


 

//       // Reset Form

//       setFormData({ name: '', dob: '', gender: 'FEMALE', contact: '', email: '', password: '', doctor: '' });

//       fetchLiveQueue();

//     } catch (error: any) {

//       // --- SWEETALERT ERROR POPUP ---

//       const errorMsg = error.response?.data?.message || "Invalid Data Format";


 

//       Swal.fire({

//         title: 'Registration Failed',

//         text: errorMsg,

//         icon: 'error',

//         background: '#1a1d21',

//         color: '#fff',

//         confirmButtonColor: '#ff4d4d'

//       });

//     }

//   };

//   // 1. Calculate Age from DOB

//   const calculateAge = (dobString: string) => {

//     if (!dobString) return 'N/A';

//     const birthDate = new Date(dobString);

//     const today = new Date();

//     let age = today.getFullYear() - birthDate.getFullYear();

//     return age;

//   };

//   const formatTime = (timeString: string) => {

//     if (!timeString) return '--:--:--';

//     const date = new Date(timeString);


 

//     return date.toLocaleTimeString([], {

//       hour: '2-digit',

//       minute: '2-digit',

//       second: '2-digit', // This adds the seconds

//       hour12: true       // This ensures AM/PM is shown

//     }).toUpperCase();

//   };

//   const handleCheckIn = async (appointmentId: number) => {

//     try {

//       // 1. Tell the backend to check in the patient

//       await axiosInstance.post(`/clinicq/receptionist/checkin/${appointmentId}`);


 

//       // 2. WAIT for the live queue to update from the server

//       // This ensures Token #4 is actually in the 'patients' array

//       await fetchLiveQueue();


 

//       // 3. WAIT for the appointments list to update

//       await fetchAppointments();


 

//       // 4. Only then show the success message

//       Swal.fire({

//         title: 'Check-in Successful!',

//         icon: 'success',

//         timer: 1500,

//         showConfirmButton: false,

//         background: '#1a1d21',

//         color: '#fff'

//       });


 

//     } catch (error: any) {

//       console.error("Check-in Error:", error);

//       Swal.fire({

//         title: 'Check-in Failed',

//         text: error.response?.data?.message || "Server error",

//         icon: 'error',

//         background: '#1a1d21',

//         color: '#fff'

//       });

//     }

//   };

//   // Update patient positions in queue

//   React.useEffect(() => {

//     const updatedPatients = patients.map(patient => {

//       const sameDoctorPatients = patients.filter(p => p.doctor === patient.doctor && p.status !== 'COMPLETED');

//       const position = sameDoctorPatients.findIndex(p => p.id === patient.id) + 1;

//       return { ...patient, position };

//     });


 

//     // Check if positions actually changed before updating

//     const positionsChanged = updatedPatients.some((patient, index) =>

//       patient.position !== patients[index]?.position

//     );


 

//     if (positionsChanged) {

//       setPatients(updatedPatients);

//     }

//   }, [patients.length]);


 

//   const movePatientToDoctor = (patientId: string, newDoctorId: string) => {

//     setPatients(prev =>

//       prev.map(patient => {

//         if (patient.id === patientId) {

//           const newQueueNumber = generateQueueNumber(newDoctorId);

//           return {

//             ...patient,

//             doctor: newDoctorId === 'general' ? undefined : newDoctorId,

//             queueNumber: newQueueNumber

//           };

//         }

//         return patient;

//       })

//     );

//   };


 

//   const reorderPatient = (patientId: string, direction: 'up' | 'down') => {

//     setPatients(prev => {

//       const patientIndex = prev.findIndex(p => p.id === patientId);

//       if (patientIndex === -1) return prev;


 

//       const patient = prev[patientIndex];

//       const sameDoctorPatients = prev.filter(p => p.doctor === patient.doctor && p.status !== 'COMPLETED');

//       const currentIndexInDoctorQueue = sameDoctorPatients.findIndex(p => p.id === patientId);


 

//       let newIndex = currentIndexInDoctorQueue;

//       if (direction === 'up' && currentIndexInDoctorQueue > 0) {

//         newIndex = currentIndexInDoctorQueue - 1;

//       } else if (direction === 'down' && currentIndexInDoctorQueue < sameDoctorPatients.length - 1) {

//         newIndex = currentIndexInDoctorQueue + 1;

//       }


 

//       if (newIndex === currentIndexInDoctorQueue) return prev;


 

//       const newPatients = [...prev];

//       const targetPatient = sameDoctorPatients[newIndex];

//       const targetIndex = prev.findIndex(p => p.id === targetPatient.id);


 

//       // Swap positions

//       [newPatients[patientIndex], newPatients[targetIndex]] = [newPatients[targetIndex], newPatients[patientIndex]];


 

//       return newPatients;

//     });

//   };


 

//   const getPatientsByDoctor = (doctorId?: string) => {

//     return patients.filter(patient =>

//       patient.doctor === doctorId && patient.status !== 'COMPLETED'

//     ).sort((a, b) => a.position - b.position);

//   };


 

//   const handleSearch = async () => {

//     if (!searchKeyword.trim()) {

//       setShowSearchResults(false);

//       setSearchResults([]);

//       return;

//     }


 

//     try {

//       const res = await axiosInstance.get(`/clinicq/receptionist/search`, {

//         params: { keyword: searchKeyword }

//       });

//       // The response is a list of Patients: [{id: 5, name: "Harry", ...}]

//       setSearchResults(res.data);

//       setShowSearchResults(true);

//     } catch (err) {

//       console.error("Search failed", err);

//     }

//   };



 

//   const handleViewPatientAppointments = async (identifier: any, patientName: string) => {

//     if(!identifier){

//       Swal.fire({title:'Error',text:'Invalid Patient ID',icon:'error',background:'#1a1d21'});

//       return;

//     }

//     try {

//       const res = await axiosInstance.get(`/clinicq/patient/appointments`);

//       const appointmentsList = res.data;

//       const data=Array.isArray(res.data) ? res.data[0]:res.data;

//       if (appointmentsList && appointmentsList.length > 0) {

//         const latest = appointmentsList[0];


 

//         // Logic to extract date and time safely

//         const bookedDate = latest.timeSlot?.slotDate

//           ? latest.timeSlot.slotDate

//           : (latest.createdAt?latest.createdAt.split('T')[0]:'N/A'); // Fallback to createdAt date


 

//         const timeSlotRange = latest.timeSlot

//           ? `${latest.timeSlot.startTime} - ${latest.timeSlot.endTime}`

//           : "Walk-in (No Slot)";


 

//         Swal.fire({

//           title: `<span style="color: #00f2fe; font-family: sans-serif;">Appointment Details</span>`,

//           html: `

//           <div style="text-align: left; color: #fff; line-height: 2; font-family: sans-serif;">

//             <p><strong>Patient Name:</strong> ${latest.patient?.name || patientName}</p>


 

//             <p><strong>Date:</strong>

//               <span style="color: #00f2fe">${bookedDate || 'N/A'}</span>

//             </p>


 

//             <p><strong>Time Slot:</strong>

//               <span style="color: #00f2fe">${timeSlotRange}</span>

//             </p>


 

//             <p><strong>Doctor:</strong> ${latest.doctor?.name || 'N/A'}</p>


 

//             <p><strong>Type:</strong>

//               <span class="badge bg-info" style="padding: 2px 8px;">${latest.type}</span>

//             </p>


 

//             <p><strong>Status:</strong>

//               <span class="badge" style="background-color: #198754; color: white; padding: 4px 12px; border-radius: 4px;">

//                 ${latest.status}

//               </span>

//             </p>

//           </div>

//         `,

//           background: '#1a1d21',

//           confirmButtonColor: '#00f2fe',

//           confirmButtonText:'OK'

//         });

//       } else {

//         Swal.fire({ title: 'No History', icon: 'info', background: '#1a1d21', color: '#fff' });

//       }

//     } catch (error) {

//       console.error("Fetch error:", error);

//     }

//   };

//   const getPatientTodayStatus = (patient: Patient) => {

//     const todayPatients = patients.filter(p =>

//       p.name === patient.name && p.contact === patient.contact

//     );


 

//     if (todayPatients.length === 0) return null;


 

//     const activePatient = todayPatients.find(p => p.status !== 'COMPLETED');

//     if (activePatient) {

//       return {

//         status: activePatient.status,

//         queueNumber: activePatient.queueNumber,

//         doctor: activePatient.doctor

//       };

//     }


 

//     return {

//       status: 'Completed',

//       queueNumber: todayPatients[0].queueNumber,

//       doctor: todayPatients[0].doctor

//     };

//   };


 

//   const createNewVisit = (patient: Patient) => {

//     // Pre-fill the form with patient data and provide defaults for new fields

//     setFormData({

//       name: patient.name,

//       contact: patient.contact || patient.phone || '',

//       // Add these new fields to match the updated formData state:

//       dob: '',

//       gender: 'MALE',

//       email: '',

//       password: '',

//       reason: '',

//       doctor: '' // Reset doctor selection for the new visit

//     });


 

//     // Switch to the walk-in registration tab

//     setActiveTab('walk-in');

//   };

//   useEffect(() => {

//     const fetchAllAppointments = async () => {

//       try {

//         // Use the receptionist-specific endpoint if available,

//         // or a general 'today' endpoint you've created.

//         const response = await axiosInstance.get('/clinicq/receptionist/appointments/today');


 

//         const mapped = response.data.map((appt: any) => ({

//           id: appt.id.toString(),

//           patientName: appt.patient.name,

//           age: 25, // Ideally calculated from appt.patient.dateOfBirth

//           contact: appt.patient.phone,

//           doctorId: appt.doctor.id.toString(),

//           doctorName: appt.doctor.name,

//           appointmentTime: appt.timeSlot.startTime,

//           date: appt.timeSlot.slotDate,

//           status: appt.status === 'BOOKED' ? 'Scheduled' : 'Checked-In',

//           reason: appt.reason

//         }));

//         setAppointments(mapped);

//       } catch (err) {

//         console.error("Error loading appointments:", err);

//       }

//     };

//     fetchAllAppointments();

//   }, []);


 

//   const fetchDoctorsByDepartment = async (deptName: string) => {

//     try {

//       const response = await axiosInstance.get(`/clinicq/patient/doctors`, { //check api url

//         params: { department: deptName }

//       });

//       // This updates the 'doctors' state with Dr. Nancy

//       setDoctors(response.data);

//     } catch (err) {

//       console.error("Error fetching doctors:", err);

//       setDoctors([]);

//     }

//   };


 

//   const fetchLiveQueue = async (docId?: string) => {

//     const doctorToFetch = docId || formData.doctor || "2"; //doctorid

//     //update

//     const token = localStorage.getItem('token');

//     try {

//       const res = await axiosInstance.get(`/clinicq/receptionist/queue/${doctorToFetch}`,{

//         headers:{'Authorization':`Bearer ${token}`}

//       });

//       setPatients(res.data);

//       //return res.data; // Return data so the await above works correctly

//     } catch (err) {

//       console.error("Fetch Queue Error", err);

//     }

//   };

//   // Call this in a useEffect or after a Check-In

//   const updatePatientStatus = async (tokenId: string, newStatus: Patient['status']) => {

//     // Map UI status back to Backend ENUM

//     const statusMap: { [key: string]: string } = {

//       'Waiting': 'WAITING',

//       'In Consultation': 'IN_CONSULTATION',

//       'Completed': 'COMPLETED',

//       'No Show': 'NO_SHOW',

//       //'Checked In':'CHECKED_IN'

//     };


 

//     try {

//       const response = await axiosInstance.put(`/clinicq/doctor/token/${tokenId}/status`, null, {

//         params: { status: statusMap[newStatus] }

//       });

//       // Refresh local state


 

//       //await fetchLiveQueue();

//       setTimeout(() => {

//         fetchLiveQueue();


 

//       }, 300);



 

//     } catch (err) {

//       alert("Could not update status on server.");

//     }

//   };

//   const fetchQueueForDoctor = async (docId: string) => {

//     try {

//       const response = await axiosInstance.get(`/clinicq/receptionist/queue/${docId}`);

//       const queueItems = response.data.map((t: any) => ({

//         id: t.id.toString(),

//         name: t.patient.name,

//         queueNumber: `TKN-${t.tokenNumber}`,

//         status: t.status, // 'WAITING', 'IN_CONSULTATION', etc.

//         timestamp: new Date(t.checkInTime)

//       }));

//       // Update your queue state

//       setPatients(queueItems);

//     } catch (err) {

//       console.error("Queue fetch failed");

//     }

//   };

//   const fetchAppointments = async () => {

//     try {

//       const res = await axiosInstance.get('/clinicq/receptionist/today');

//       setAppointments(res.data); // This fills the 'appointments' state

//     } catch (err) {

//       console.error("Error fetching pre-booked appointments:", err);

//     }

//   };

//   useEffect(() => {

//     const init = async () => {

//       // Fetch pre-booked appointments for the top table

//       await fetchAppointments();


 

//       // Fetch the live queue for the bottom table

//       // If no doctor is selected, we should default to Dr. Nancy (ID: 1)

//       // so the table isn't empty on login.

//       const doctorId = formData.doctor || "1"; //doctorid

//       fetchLiveQueue(doctorId);

//     };

//     init();

//   }, [formData.doctor]); // Runs once when you log in

//   useEffect(() => {

//     fetchAppointments();

//   }, []);

//   return (

//     <div className="container-fluid bg-dark min-vh-100 text-white p-4">

//       <div className="row">

//         <div className="col-12">

//           <h1 className="text-center mb-4 text-info">Receptionist Portal</h1>

//         </div>

//       </div>


 

//       <div className="row mb-4">

//         <div className="col-12">

//           <ul className="nav nav-tabs nav-fill">

//             <li className="nav-item">

//               <button

//                 className={`nav-link ${activeTab === 'walk-in' ? 'active bg-info text-dark' : 'text-white'}`}

//                 onClick={() => setActiveTab('walk-in')}

//               >

//                 Walk-in Registration

//               </button>

//             </li>

//             <li className="nav-item">

//               <button

//                 className={`nav-link ${activeTab === 'appointments' ? 'active bg-info text-dark' : 'text-white'}`}

//                 onClick={() => setActiveTab('appointments')}

//               >

//                 Today's Appointments

//               </button>

//             </li>

//             <li className="nav-item">

//               <button

//                 className={`nav-link ${activeTab === 'queue-management' ? 'active bg-info text-dark' : 'text-white'}`}

//                 onClick={() => setActiveTab('queue-management')}

//               >

//                 Queue Management

//               </button>

//             </li>

//             <li className="nav-item">

//               <button

//                 className={`nav-link ${activeTab === 'now-serving' ? 'active bg-info text-dark' : 'text-white'}`}

//                 onClick={() => setActiveTab('now-serving')}

//               >

//                 Now Serving

//               </button>

//             </li>

//             <li className="nav-item">

//               <button

//                 className={`nav-link ${activeTab === 'search' ? 'active bg-info text-dark' : 'text-white'}`}

//                 onClick={() => setActiveTab('search')}

//               >

//                 Search Patients

//               </button>

//             </li>

//           </ul>

//         </div>

//       </div>


 

//       {activeTab === 'walk-in' && (

//         <div className="row justify-content-center">

//           <div className="col-lg-8 mb-4">

//             <div className="card bg-secondary border-info shadow">

//               <div className="card-header bg-info text-dark">

//                 <h3 className="mb-0 fs-5">Register Walk-in Patient</h3>

//               </div>

//               <div className="card-body">

//                 <form onSubmit={handleSubmit}>

//                   <div className="row g-3">

//                     {/* Name and Gender */}

//                     <div className="col-md-8">

//                       <label className="form-label text-white small">Patient Name *</label>

//                       <input type="text" className="form-control form-control-sm" name="name" value={formData.name} onChange={handleInputChange} required />

//                     </div>

//                     <div className="col-md-4">

//                       <label className="form-label text-white small">Gender *</label>

//                       <select

//                         className="form-select form-select-sm"

//                         name="gender"

//                         value={formData.gender}

//                         onChange={handleInputChange}

//                       >

//                         <option value="FEMALE">Female</option>

//                         <option value="MALE">Male</option>

//                       </select>

//                     </div>


 

//                     {/* DOB and Age */}

//                     <div className="col-12">

//                       <label className="form-label text-white small">Date of Birth *</label>

//                       <input type="date" className="form-control form-control-sm" name="dob" value={formData.dob} onChange={handleInputChange} required />

//                     </div>


 

//                     {/* Contact and Email */}

//                     <div className="col-md-6">

//                       <label className="form-label text-white small">Phone Number *</label>

//                       <input type="tel" className="form-control form-control-sm" name="contact" value={formData.contact} onChange={handleInputChange} required />

//                     </div>

//                     <div className="col-md-6">

//                       <label className="form-label text-white small">Email *</label>

//                       <input type="email" className="form-control form-control-sm" name="email" value={formData.email} onChange={handleInputChange} required />

//                     </div>


 

//                     {/* Password */}

//                     <div className="col-12">

//                       <label className="form-label text-white small">Portal Password *</label>

//                       <input type="password" placeholder="Create a temporary password" className="form-control form-control-sm" name="password" value={formData.password} onChange={handleInputChange} required />

//                     </div>


 

//                     {/* Reason */}

//                     <div className="col-12">

//                       <label className="form-label text-white small">Reason for Visit</label>

//                       <textarea className="form-control form-control-sm" name="reason" value={formData.reason} onChange={handleInputChange} rows={2} />

//                     </div>


 

//                     <div className="row g-3">

//                       {/* SELECT DEPARTMENT */}

//                       <div className="col-md-6">

//                         <label className="form-label text-white small">Select Department *</label>

//                         <select

//                           className="form-select form-select-sm"

//                           value={selectedDept}

//                           onChange={(e) => {

//                             const dept = e.target.value;

//                             setSelectedDept(dept);

//                             setFormData(prev => ({ ...prev, doctor: '' }));


 

//                             if (dept) {

//                               fetchDoctorsByDepartment(dept); // GETS DOCTORS FROM BACKEND NOW

//                             } else {

//                               setDoctors([]);

//                             }

//                           }}

//                           required

//                         >

//                           <option value="">Choose Department...</option>

//                           {DEPARTMENTS.map(dept => (

//                             <option key={dept} value={dept}>{dept}</option>

//                           ))}

//                         </select>

//                       </div>


 

//                       {/* SELECT DOCTOR */}

//                       <div className="col-md-6">

//                         <label className="form-label text-white small">Select Doctor *</label>

//                         <select

//                           className="form-select form-select-sm"

//                           name="doctor"

//                           value={formData.doctor}

//                           onChange={handleInputChange}

//                           disabled={!selectedDept || doctors.length === 0}

//                           required

//                         >

//                           <option value="">Select Doctor...</option>

//                           {doctors.map(doc => (

//                             <option key={doc.id} value={doc.id}>

//                               {doc.name}

//                             </option>

//                           ))}

//                         </select>


 

//                         {/* Show this ONLY if the API call finished and returned nothing */}

//                         {selectedDept && doctors.length === 0 && (

//                           <div className="text-danger mt-1" style={{ fontSize: '10px' }}>

//                             No doctors currently available in {selectedDept}

//                           </div>

//                         )}

//                       </div>

//                     </div>

//                   </div>


 

//                   <button type="submit" className="btn btn-info btn-sm w-100 fw-bold">

//                     REGISTER & GENERATE TOKEN

//                   </button>

//                 </form>

//               </div>

//             </div>

//           </div>

//         </div>

//       )}


 

//       {activeTab === 'appointments' && (

//         <div className="row">

//           <div className="col-12">

//             <div className="card bg-secondary border-info">

//               <div className="card-header bg-info text-dark">

//                 <h3 className="mb-0">Today's Appointments</h3>

//               </div>

//               <div className="card-body">

//                 {appointments.length === 0 ? (

//                   <p className="text-center text-muted">No appointments scheduled for today</p>

//                 ) : (

//                   <div className="table-responsive">

//                     <table className="table table-dark table-hover">

//                       <thead>

//                         <tr>

//                           <th>Time</th>

//                           <th>Patient Name</th>

//                           <th>Age</th>

//                           <th>Contact</th>

//                           <th>Doctor</th>

//                           <th>Reason</th>

//                           <th>Status</th>

//                           <th>Action</th>

//                         </tr>

//                       </thead>

//                       <tbody>

//                         {appointments.map((appt: any) => (

//                           <tr key={appt.id}

//                             className={appt.status === 'CHECKED_IN' ? 'bg-dark-highlight' : ''}

//                             style={appt.status === 'CHECKED_IN' ? { backgroundColor: 'rgba(0, 242, 254, 0.05)' } : {}}>

//                             {/* Time: Use createdAt from the appointment object */}

//                             <td>{formatTime(appt.createdAt)}</td>


 

//                             {/* Patient Name: Found inside the patient object */}

//                             <td className="fw-bold">{appt.patient?.name || 'Unknown'}</td>


 

//                             {/* Age: Calculated from patient.dateOfBirth */}

//                             <td>{calculateAge(appt.patient?.dateOfBirth)}</td>


 

//                             {/* Contact: Found inside patient object */}

//                             <td>{appt.patient?.phone || 'N/A'}</td>


 

//                             {/* Doctor: Found inside doctor object */}

//                             <td>{appt.doctor?.name || 'N/A'}</td>


 

//                             {/* Reason: Straight from appointment object */}

//                             <td>{appt.reason || 'General Consultation'}</td>


 

//                             {/* Status Column: Badge + Token Box */}

//                             <td>

//                               <div className="d-flex align-items-center gap-2">

//                                 <span className={`badge ${appt.status === 'CHECKED_IN' ? 'bg-success' : 'bg-danger'}`}>

//                                   {appt.status}

//                                 </span>


 

//                                 {/* Display Token if checked in */}

//                                 {appt.status === 'CHECKED_IN' && (

//                                   <div style={{

//                                     background: '#00f2fe',

//                                     color: '#000',

//                                     padding: '2px 8px',

//                                     borderRadius: '4px',

//                                     fontSize: '0.7rem',

//                                     fontWeight: 'bold',

//                                     boxShadow: '0 0 5px #00f2fe'

//                                   }}>

//                                     {/* T-{

//                                       (patients as any[]).find(t =>

//                                         t.patient?.id === appt.patient?.id && t.doctor?.id === appt.doctor?.id

//                                       )?.tokenNumber || '...'

//                                     } */}

//                                     T-{

//                                       // CHANGE: Match by appt.id to ensure each row gets its unique token

//                                       (patients as any[]).find((t) => t.appointment?.id === appt.id)?.tokenNumber || '...'

//                                     }

//                                   </div>

//                                 )}

//                               </div>

//                             </td>


 

//                             {/* Action Column: The Missing Button */}

//                             <td>

//                               {appt.status !== 'CHECKED_IN' ? (

//                                 <button

//                                   className="btn btn-sm btn-outline-info"

//                                   onClick={() => handleCheckIn(appt.id)}

//                                   //disabled={appt.status==='COMPLETED' || appt.status==='NO_SHOW'}

//                                   style={{ fontSize: '0.7rem', padding: '2px 10px' }}

//                                 >

//                                   CHECK IN

//                                 </button>

//                               ) : (

//                                 <span className="text-info fw-bold small text-uppercase" style={{ letterSpacing: '1px' }}>

//                                   In-Queue

//                                 </span>

//                               )}

//                             </td>

//                           </tr>

//                         ))}

//                       </tbody>

//                     </table>

//                   </div>

//                 )}

//               </div>

//             </div>

//           </div>

//         </div>

//       )}


 

//       {activeTab === 'queue-management' && (

//         <div className="row">

//           {doctors.map((doctor) => {

//             const doctorPatients = getPatientsByDoctor(doctor.id);

//             return (

//               <div key={doctor.id} className="col-lg-6 mb-4">

//                 <div className="card bg-secondary border-info">

//                   <div className="card-header bg-info text-dark">

//                     <h5 className="mb-0">{doctor.name}</h5>

//                     <small>{doctor.specialization}</small>

//                   </div>

//                   <div className="card-body">

//                     {doctorPatients.length === 0 ? (

//                       <p className="text-center text-muted">No patients in queue</p>

//                     ) : (

//                       <div className="table-responsive">

//                         <table className="table table-dark table-sm">

//                           <thead>

//                             <tr>

//                               <th>#</th>

//                               <th>Queue #</th>

//                               <th>Name</th>

//                               <th>Age</th>

//                               <th>Status</th>

//                               <th>Time</th>

//                               <th>Actions</th>

//                             </tr>

//                           </thead>

//                           <tbody>

//                             {doctorPatients.map((patient, index) => (

//                               <tr key={patient.id}>

//                                 <td>{index + 1}</td>

//                                 <td className="text-info fw-bold">{patient.queueNumber}</td>

//                                 <td>{patient.name}</td>

//                                 <td>{patient.age}</td>

//                                 <td>

//                                   <select

//                                     className="form-select form-select-sm"

//                                     value={patient.status}

//                                     onChange={(e) => updatePatientStatus(patient.id, e.target.value as Patient['status'])}

//                                   >

//                                     <option value="Waiting">Waiting</option>

//                                     <option value="In Consultation">In Consultation</option>

//                                     <option value="Completed">Completed</option>

//                                     <option value="No Show">No Show</option>

//                                   </select>

//                                 </td>

//                                 <td>{new Date(patient.timestamp).toLocaleTimeString()}</td>

//                                 <td>

//                                   <div className="btn-group btn-group-sm" role="group">

//                                     <button

//                                       className="btn btn-outline-info"

//                                       onClick={() => reorderPatient(patient.id, 'up')}

//                                       disabled={index === 0}

//                                     >

//                                       ↑

//                                     </button>

//                                     <button

//                                       className="btn btn-outline-info"

//                                       onClick={() => reorderPatient(patient.id, 'down')}

//                                       disabled={index === doctorPatients.length - 1}

//                                     >

//                                       ↓

//                                     </button>

//                                     <select

//                                       className="form-select form-select-sm"

//                                       onChange={(e) => movePatientToDoctor(patient.id, e.target.value)}

//                                       defaultValue=""

//                                     >

//                                       <option value="" disabled>Move to...</option>

//                                       {doctors.filter(d => d.id !== doctor.id).map(d => (

//                                         <option key={d.id} value={d.id}>{d.name}</option>

//                                       ))}

//                                     </select>

//                                   </div>

//                                 </td>

//                               </tr>

//                             ))}

//                           </tbody>

//                         </table>

//                       </div>

//                     )}

//                   </div>

//                 </div>

//               </div>

//             );

//           })}

//         </div>

//       )}


 

//       {activeTab === 'now-serving' && (

//         <NowServing patients={patients} doctors={doctors} />

//       )}


 

//       {activeTab === 'search' && (

//         <div className="row">

//           <div className="col-12">

//             <div className="card bg-secondary border-info">

//               <div className="card-header bg-info text-dark">

//                 <h3 className="mb-0">Search Patients</h3>

//               </div>

//               <div className="card-body">

//                 {/* Search Bar */}

//                 <div className="mb-4">

//                   <div className="input-group">

//                     <input

//                       type="text"

//                       className="form-control"

//                       placeholder="Search by patient name or phone number..."

//                       value={searchKeyword}

//                       onChange={(e) => setSearchKeyword(e.target.value)}

//                       onKeyPress={(e) => e.key === 'Enter' && handleSearch()}

//                     />

//                     <button className="btn btn-info" onClick={handleSearch} type="button">
//                       <i className="fas fa-search"></i> Search

//                     </button>

//                   </div>

//                 </div>


 

//                 {/* Search Results */}

//                 {showSearchResults && (

//                   <div className="table-responsive mt-3">

//                     <table className="table table-dark table-hover mb-0">

//                       <thead>

//                         <tr>

//                           <th>Queue #</th>

//                           <th>Name</th>

//                           <th>Age</th>

//                           <th>Contact</th>

//                           <th>Doctor</th>

//                           <th>Type</th>

//                           <th>Status</th>

//                           <th>Time</th>

//                           <th>Actions</th>

//                         </tr>

//                       </thead>

//                       <tbody>


 

//                         {searchResults.length > 0 ? (

//                           searchResults.map((item:any,index:number) => {

//                             // 1. Find if this patient is currently in the live queue

//                             //const pId=patient.id;

//                             //const queueRecord = patients.find((t:any) => (t.patient?.id || t.id) === pId);

//                             const rowKey=item.id || item.tokenDisplay || `patient-${index}`;

//                             return (

//                               <tr key={rowKey}>

//                                 {/* Display Queue Record data if found, otherwise N/A */}

//                                 {/* <td className="text-info fw-bold">{queueRecord ? `#${queueRecord.tokenNumber}` : '-'}</td>

//                                 <td className="fw-bold">{patient.name}</td>

//                                 <td>{patient.dateOfBirth? calculateAge(patient.dateOfBirth):(patient.age || 'N/A')}</td>

//                                 <td>{patient.phone || patient.contact}</td>


 

//                                 <td>{queueRecord?.doctor?.name || 'N/A'}</td>

//                                  */}

//                                  <td className="text-info fw-bold">{item.tokenDisplay ||'-'}</td>

//                                 <td className="fw-bold">{item.patientName || 'N/A'}</td>

//                                 <td>{item.dateOfBirth? calculateAge(item.dateOfBirth):'N/A'}</td>

//                                 <td>{item.phone || item.contact}</td>

//                                 <td>{item.doctorName || 'N/A'}</td>

//                                 <td>

//                                   <span className="badge bg-info">{item.appointmentType || 'WALK_IN'}</span>

//                                 </td>

//                                 <td>

//                                   <span className="badge bg-warning text-dark">{item.appointmentStatus || 'Not in Queue'}</span>

//                                 </td>

//                                 <td>{item.checkInTime? formatTime(item.checkInTime) : '--:--'}</td>



 

//                                 {/* <td>

//                                   {queueRecord ? (

//                                     <span className="badge bg-info">{queueRecord.appointment?.type || 'WALK_IN'}</span>

//                                   ) : 'N/A'}

//                                 </td>

//                                 <td>

//                                   {queueRecord ? (

//                                     <span className="badge bg-warning text-dark">{queueRecord.status}</span>

//                                   ) : (

//                                     <span className="badge bg-secondary opacity-50">Not in Queue</span>

//                                   )}

//                                 </td>

//                                 <td>{queueRecord ? formatTime(queueRecord.checkInTime) : '--:--'}</td> */}


 

//                                 {/* ACTIONS COLUMN */}

//                                 <td>

//                                   <div className="d-flex gap-2">

//                                     <button

//                                       className="btn btn-sm btn-info"

//                                       style={{ fontSize: '0.75rem', padding: '2px 8px' }}

//                                       // PASS BOTH ID AND NAME

//                                       onClick={() => {

//                                         const pId=item.id || item.patientId || item.tokenDisplay;

//                                         if(pId){

//                                           handleViewPatientAppointments(pId,item.patientName || item.name);

//                                         }else{

//                                           Swal.fire({

//                                             title:'Error',

//                                             text:'No unique identifier',

//                                             icon:'warning',

//                                             background:'#1a1d21',

//                                             color:'#fff'

//                                           });

//                                         }



 

//                                       }}

//                                     >

//                                       Appointment

//                                     </button>


 

//                                     <button

//                                       className="btn btn-sm btn-outline-success"

//                                       style={{ fontSize: '0.75rem', padding: '2px 8px' }}

//                                       onClick={() => {

//                                         setFormData({

//                                           ...formData,

//                                           name: item.patientName,

//                                           contact: item.phone,

//                                           dob: item.dateOfBirth

//                                         });

//                                         setActiveTab('walk-in');

//                                       }}

//                                     >

//                                       New Visit

//                                     </button>

//                                   </div>

//                                 </td>

//                               </tr>

//                             );

//                           })

//                         ) : (

//                           <tr>

//                             <td colSpan={9} className="text-center py-4 text-muted">No patient found in database.</td>

//                           </tr>

//                         )}

//                       </tbody>

//                     </table>

//                   </div>

//                 )}

//               </div>

//             </div>

//           </div>

//         </div>

//       )}


 

//       {/* Current Queue - Always visible */}

//       <div className="row mt-4">

//         <div className="col-12">

//           <div className="card bg-secondary border-info">

//             <div className="card-header bg-info text-dark">

//               <h3 className="mb-0">Current Queue</h3>

//             </div>

//             <div className="card-body">

//               {patients.length === 0 ? (

//                 <p className="text-center text-muted">No patients in queue</p>

//               ) : (

//                 <div className="table-responsive">

//                   <table className="table table-dark table-hover">

//                     <thead>

//                       <tr>

//                         <th>Queue #</th>

//                         <th>Name</th>

//                         <th>Age</th>

//                         <th>Contact</th>

//                         <th>Doctor</th>

//                         <th>Type</th>

//                         <th>Status</th>

//                         <th>Time</th>

//                       </tr>

//                     </thead>

//                     <tbody>

//                       {patients.length > 0 ? (

//                         patients.map((token: any) => (

//                           <tr key={token.id}>

//                             {/* Ensure you use token.patient.name as we found earlier */}

//                             <td className="text-info fw-bold">#{token.tokenNumber}</td>

//                             <td>{token.patient?.name}</td>

//                             <td>{calculateAge(token.patient?.dateOfBirth)}</td>

//                             <td>{token.patient?.phone}</td>

//                             <td>{token.doctor?.name}</td>

//                             <td>

//                               <span className="badge bg-info">{token.appointment?.type}</span>

//                             </td>

//                             <td>

//                               <span className="badge bg-warning">{token.status}</span>

//                             </td>

//                             <td>{formatTime(token.checkInTime)}</td>

//                           </tr>

//                         ))

//                       ) : (

//                         <tr>

//                           <td colSpan={8} className="text-center py-4 text-muted">

//                             No patients currently in queue for this doctor.

//                           </td>

//                         </tr>

//                       )}

//                     </tbody>

//                   </table>

//                 </div>

//               )}

//             </div>

//           </div>

//         </div>

//       </div>

//     </div>

//   );

// };


 

// export default ReceptionistDashboard;





 

import React, { useState, useEffect, useCallback } from 'react';


 

import axios from 'axios';


 

import NowServing from './NowServing';


 

import Swal from 'sweetalert2';


 

import axiosInstance from '../../../services/axiosInstance';





 

// --- INTERFACES (STRICTLY UNTOUCHED) ---


 

interface Token {


 

  id: number;


 

  tokenNumber: number;


 

  status: string;


 

  checkInTime: string;


 

  patient: { id: number; name: string; phone: string; dateOfBirth: string; };


 

  doctor: { id: number; name: string; };


 

  appointment?: { type: string; };


 

}





 

interface Appointment {


 

  id: number;


 

  patientName: string;


 

  age: number;


 

  contact: string;


 

  doctorId: string;


 

  doctorName: string;


 

  appointmentTime: string;


 

  date: string;


 

  status: 'Scheduled' | 'Checked-In' | 'Completed' | 'Cancelled';


 

  reason?: string;


 

  queueNumber?: string;


 

}





 

export interface Patient {


 

  id: any;


 

  name: string;


 

  age: number;


 

  phone?: string;


 

  contact?: string;


 

  dateOfBirth?: string;


 

  reason: string;


 

  queueNumber: string;


 

  doctor?: string;


 

  timestamp: Date;


 

  type: 'walk-in' | 'appointment';


 

  appointmentId?: string;


 

  status: 'WAITING' | 'IN_CONSULTATION' | 'COMPLETED' | 'NO_SHOW';


 

  position: number;


 

}





 

export interface Doctor {


 

  id: string;


 

  name: string;


 

  specialization?: string;


 

  department: string;


 

}





 

const ReceptionistDashboard: React.FC = () => {


 

  // --- STATE (STRICTLY UNTOUCHED) ---


 

  const [patients, setPatients] = useState<any[]>([]);


 

  const [appointments, setAppointments] = useState<Appointment[]>([]);


 

  const [activeTab, setActiveTab] = useState<'walk-in' | 'appointments' | 'queue-management' | 'now-serving' | 'search'>('walk-in');


 

  const [queueCounter, setQueueCounter] = useState<{ [key: string]: number }>({});


 

  const [showSearchResults, setShowSearchResults] = useState(false);


 

  const DEPARTMENTS = ["GENERAL", "CARDIOLOGY", "ORTHOPEDICS", "PEDIATRICS"];


 

  const [selectedDept, setSelectedDept] = useState<string>('');


 

  const [searchKeyword, setSearchKeyword] = useState('');


 

  const [searchResults, setSearchResults] = useState<any[]>([]);


 

  const [formData, setFormData] = useState({


 

    name: '', dob: '', gender: 'MALE', contact: '', email: '', password: '', doctor: '', reason: ''


 

  });


 

  const [doctors, setDoctors] = useState<Doctor[]>([]);

  const [isFormValid, setIsFormValid] = useState(false);

  //validation regex

  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

  const phoneRegex = /^[0-9]{10}$/;


 

  const isValidAge = (dobString: string) => {

    if (!dobString) return false;

    const today = new Date();

    const dob = new Date(dobString);

    return dob <= today;

  };


 

  // --- LOGIC HANDLERS ---


 

  const fetchAppointments = useCallback(async () => {


 

    try {


 

      const res = await axiosInstance.get('/clinicq/receptionist/today');


 

      setAppointments(res.data);


 

    } catch (err) { console.error("Error fetching appointments:", err); }


 

  }, []);





 

  const fetchLiveQueue = useCallback(async (docId?: string) => {


 

    const doctorToFetch = docId || formData.doctor || "1";


 

    const token = localStorage.getItem('token');


 

    try {


 

      const res = await axiosInstance.get(`/clinicq/receptionist/queue/${doctorToFetch}`, {


 

        headers: { 'Authorization': `Bearer ${token}` }


 

      });


 

      setPatients(res.data);


 

    } catch (err) { console.error("Fetch Queue Error", err); }


 

  }, [formData.doctor]);





 

  const fetchDoctorsByDepartment = async (deptName: string) => {


 

    try {


 

      const response = await axiosInstance.get(`/clinicq/patient/doctors`, { params: { department: deptName } });


 

      setDoctors(response.data);


 

    } catch (err) { setDoctors([]); }


 

  };





 

  useEffect(() => {


 

    fetchAppointments();


 

    fetchLiveQueue();


 

  }, [fetchAppointments, fetchLiveQueue]);


 

  useEffect(() => {

    const { name, dob,contact,email,password,doctor } = formData;

   

    const isNameOk = name.trim().length>=2;

    const isDobOk=isValidAge(dob);

    const isEmailOk = emailRegex.test(email);

    const isPassOk = passwordRegex.test(password);

    const isPhoneOk = phoneRegex.test(contact);

    const isSelectionOk=selectedDept!=="" && doctor!=="";


 

    // We call this whenever selectedDays or the form state might change

    setIsFormValid(isNameOk && isDobOk && isPhoneOk && isEmailOk && isPassOk && isSelectionOk);

  }, [formData,selectedDept]);





 

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {


 

    const { name, value } = e.target;


 

    setFormData(prev => ({ ...prev, [name]: value }));


 

  };





 

  const handleSubmit = async (e: React.FormEvent) => {


 

    e.preventDefault();


 

    if (formData.password.length < 6) return alert("Password must be at least 6 characters.");


 

    const patientDTO = {


 

      name: formData.name, dateOfBirth: formData.dob, gender: formData.gender, phone: formData.contact,


 

      user: { email: formData.email, password: formData.password, role: "PATIENT" }


 

    };


 

    try {


 

      const response = await axiosInstance.post(`/clinicq/receptionist/walkin/${formData.doctor}`, patientDTO);


 

      Swal.fire({ title: 'Token Generated!', html: `<h2 style="color:#f2994a">#${response.data.tokenNumber}</h2>`, icon: 'success', confirmButtonColor: '#f2994a' });


 

      setFormData({ name: '', dob: '', gender: 'MALE', contact: '', email: '', password: '', doctor: '', reason: '' });


 

      fetchLiveQueue();


 

    } catch (error: any) { Swal.fire('Error', 'Registration Failed', 'error'); }


 

  };





 

  const handleCheckIn = async (appointmentId: number) => {


 

    try {


 

      await axiosInstance.post(`/clinicq/receptionist/checkin/${appointmentId}`);


 

      await fetchLiveQueue();


 

      await fetchAppointments();


 

      Swal.fire({ title: 'Checked In', icon: 'success', timer: 1000, showConfirmButton: false });


 

    } catch (error) { console.error(error); }


 

  };





 

  const updatePatientStatus = async (tokenId: string, newStatus: string) => {


 

    const statusMap: any = { 'Waiting': 'WAITING', 'In Consultation': 'IN_CONSULTATION', 'Completed': 'COMPLETED', 'No Show': 'NO_SHOW' };


 

    try {


 

      await axiosInstance.put(`/clinicq/doctor/token/${tokenId}/status`, null, { params: { status: statusMap[newStatus] } });


 

      setTimeout(() => fetchLiveQueue(), 300);


 

    } catch (err) { console.error(err); }


 

  };





 

  const handleSearch = async () => {


 

    if (!searchKeyword.trim()) return;


 

    try {


 

      const res = await axiosInstance.get(`/clinicq/receptionist/search`, { params: { keyword: searchKeyword } });


 

      setSearchResults(res.data);


 

      setShowSearchResults(true);


 

    } catch (err) { console.error(err); }


 

  };





 

  // --- UPDATED HISTORY HANDLER WITH POPUP ---


 

  const handleViewPatientAppointments = async (patientId: number, patientName: string) => {


 

    try {


 

      // Logic from your specific request code


 

      const res = await axiosInstance.get(`/clinicq/patients/appointments/${patientId}`);


 

      const appointmentsList = res.data;





 

      if (appointmentsList && appointmentsList.length > 0) {


 

        const latest = appointmentsList[0];





 

        // Safely extract date and time


 

        const bookedDate = latest.timeSlot?.slotDate


 

          ? latest.timeSlot.slotDate


 

          : latest.createdAt?.split('T')[0];





 

        const timeSlotRange = latest.timeSlot


 

          ? `${latest.timeSlot.startTime} - ${latest.timeSlot.endTime}`


 

          : "Walk-in (No Slot)";





 

        Swal.fire({


 

          title: `<span style="color: #f2994a; font-family: sans-serif;">Appointment Details</span>`,


 

          html: `


 

          <div style="text-align: left; color: #333; line-height: 2; font-family: sans-serif;">


 

            <p><strong>Patient Name:</strong> ${latest.patient?.name || patientName}</p>


 

            <p><strong>Date:</strong> <span style="color: #f2994a">${bookedDate || 'N/A'}</span></p>


 

            <p><strong>Time Slot:</strong> <span style="color: #f2994a">${timeSlotRange}</span></p>


 

            <p><strong>Doctor:</strong> ${latest.doctor?.name || 'N/A'}</p>


 

            <p><strong>Type:</strong> <span class="badge bg-info" style="padding: 2px 8px;">${latest.type}</span></p>


 

            <p><strong>Status:</strong>


 

              <span class="badge" style="background-color: #198754; color: white; padding: 4px 12px; border-radius: 4px;">


 

                ${latest.status}


 

              </span>


 

            </p>


 

          </div>


 

        `,


 

          background: '#fff',


 

          confirmButtonColor: '#f2994a',


 

        });


 

      } else {


 

        Swal.fire({


 

          title: 'No History',


 

          text: 'No previous appointments found for this patient.',


 

          icon: 'info',


 

          background: '#fff',


 

          confirmButtonColor: '#f2994a'


 

        });


 

      }


 

    } catch (error) {


 

      console.error("Fetch error:", error);


 

      Swal.fire('Error', 'Could not retrieve patient history', 'error');


 

    }


 

  };





 

  const calculateAge = (dob: string) => dob ? new Date().getFullYear() - new Date(dob).getFullYear() : 'N/A';


 

  const formatTime = (ts: string) => ts ? new Date(ts).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: true }) : '--:--';


 

  const getPatientsByDoctor = (docId: string) => patients.filter(p => p.doctor?.id.toString() === docId.toString());





 

  return (


 

    <div className="d-flex min-vh-100 overflow-hidden"


 

      style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>





 

      <style>


 

        {`


 

          .glass-card-rec { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(25px); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); }


 

          .sidebar-rec { width: 280px; background: #ffffff; border-right: 1px solid #eee; display: flex; flex-direction: column; padding: 24px; }


 

          .nav-btn-rec { width: 100%; text-align: left; padding: 14px 20px; border-radius: 14px; border: none; background: transparent; font-weight: 700; color: #6c757d; margin-bottom: 8px; transition: 0.2s; }


 

          .nav-btn-active { background: rgba(242, 153, 74, 0.1); color: #f2994a; }


 

          .form-control-rec { background: #ffffff !important; border: 1px solid #dee2e6 !important; border-radius: 10px; padding: 10px 15px; font-weight: 500; }


 

          .form-control-rec:focus { border-color: #f2994a; box-shadow: 0 0 0 3px rgba(242, 153, 74, 0.1); outline: none; }


 

          .table-rec thead th { color: #adb5bd; text-transform: uppercase; font-size: 0.7rem; letter-spacing: 1px; border-bottom: 2px solid #f1f3f5; }


 

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


 

        `}


 

      </style>





 

      {/* SIDEBAR */}


 

      <div className="sidebar-rec shadow-sm">


 

        <div className="d-flex align-items-center mb-5 ps-2">


 

          <div className="rounded-circle me-3 d-flex align-items-center justify-content-center text-white fw-bold shadow-sm"


 

            style={{ width: '45px', height: '45px', background: '#f2994a' }}>👥</div>


 

          <h5 className="mb-0 fw-bold">ClinicQ <span style={{ color: '#f2994a' }}>Staff</span></h5>


 

        </div>





 

        <div className="flex-grow-1">


 

          <button onClick={() => setActiveTab('walk-in')} className={`nav-btn-rec ${activeTab === 'walk-in' ? 'nav-btn-active' : ''}`}>📝 Walk-in Reg</button>


 

          <button onClick={() => setActiveTab('appointments')} className={`nav-btn-rec ${activeTab === 'appointments' ? 'nav-btn-active' : ''}`}>📅 Today's Bookings</button>


 

          <button onClick={() => setActiveTab('queue-management')} className={`nav-btn-rec ${activeTab === 'queue-management' ? 'nav-btn-active' : ''}`}>🔄 Queue Management</button>


 

          <button onClick={() => setActiveTab('now-serving')} className={`nav-btn-rec ${activeTab === 'now-serving' ? 'nav-btn-active' : ''}`}>📢 Now Serving</button>


 

          <button onClick={() => setActiveTab('search')} className={`nav-btn-rec ${activeTab === 'search' ? 'nav-btn-active' : ''}`}>🔍 Search Patients</button>


 

          <button className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-3 px-3 ms-auto"

            style={{

              fontSize: '16px',

              display: 'flex',

              alignItems: 'center',

              gap: '8px'

            }}

            onClick={() => window.location.reload()}>📤 Log Out</button>


 

        </div>


 

      </div>





 

      {/* MAIN AREA */}


 

      <div className="flex-grow-1 p-4 overflow-auto">




 

        {/* WALK-IN REGISTRATION */}


 

        {activeTab === 'walk-in' && (


 

          <div className="glass-card-rec p-5 animate-in">


 

            <h4 className="fw-bold mb-4" style={{ color: '#f2994a' }}>Walk-in Intake</h4>


 

            <form onSubmit={handleSubmit}>


 

              <div className="row g-4">


 

                <div className="col-md-6"><label className="small fw-bold text-muted mb-2">FULL NAME</label>

                <input type="text" placeholder='Enter patient name...' className="form-control form-control-rec" name="name" value={formData.name} onChange={handleInputChange} required />

                {formData.name && formData.name.length<2 && (

                  <small className='text-danger' style={{fontSize:'9px'}}>* Name should be at least 2 letters.</small>

                )}

                </div>


 

                <div className="col-md-3">

                  <label className="small fw-bold text-muted mb-2">GENDER</label>

                  <select className="form-select form-control-rec" name="gender" value={formData.gender} onChange={handleInputChange}>

                    <option value="MALE">Male</option>

                    <option value="FEMALE">Female</option>

                  </select>

                </div>


 

                <div className="col-md-3">

                  <label className="small fw-bold text-muted mb-2">DOB</label>

                  <input type="date" className="form-control form-control-rec" name="dob" value={formData.dob} onChange={handleInputChange} max={new Date().toISOString().split("T")[0]} required />

                  {formData.dob && new Date(formData.dob)>new Date() && (

                    <small className='text-danger' style={{fontSize:'9px'}}>* Future dates not allowed.</small>

                  )}

                </div>


 

                <div className="col-md-6">

                  <label className="small fw-bold text-muted mb-2">PHONE</label>

                  <input type="tel" placeholder='Enter phone no...' className="form-control form-control-rec" name="contact" value={formData.contact} onChange={handleInputChange} maxLength={10} required />

                  {formData.contact && !phoneRegex.test(formData.contact) && (

                    <small className='text-danger' style={{fontSize:'9px'}}>* Enter 10 digits</small>

                  )}

                </div>


 

                <div className="col-md-6">

                  <label className="small fw-bold text-muted mb-2">EMAIL</label>

                  <input type="email" placeholder='Enter email id...' className="form-control form-control-rec" name="email" value={formData.email} onChange={handleInputChange} required />

                  {formData.email && !emailRegex.test(formData.email) && (

                    <small className='text-danger' style={{fontSize:'9px'}}>* Enter a valid email</small>

                  )}

                  </div>


 

                <div className="col-12"><label className="small fw-bold text-muted mb-2">SECURE PASSWORD</label>

                <input type="password" placeholder='Enter password here' name="password" className="form-control form-control-rec" value={formData.password} onChange={handleInputChange} required />

                {formData.password && !passwordRegex.test(formData.password) && (

                  <small className='text-danger' style={{fontSize:'9px'}}>* Use 8+ characters with letters,numbers and special characters.</small>

                )}

                </div>


 

                <div className='col-12'>

                  <label className='small fw-bold text-muted mb-2'>REASON FOR VISIT (OPTIONAL)</label>

                  <textarea className='form-control form-control-rec'

                    name='reason'

                    value={formData.reason}

                    onChange={handleInputChange}

                    rows={2}

                    placeholder='e.g. Regular checkup, mild fever, etc.' />

                </div>


 

                <div className="col-md-6">

                  <label className="small fw-bold text-muted mb-2">DEPARTMENT</label>

                  <select className="form-select form-control-rec" onChange={(e) => { setSelectedDept(e.target.value); fetchDoctorsByDepartment(e.target.value); }} required>

                    <option value="">Select Dept</option>

                    {DEPARTMENTS.map(d => <option key={d} value={d}>{d}</option>)}

                  </select>

                </div>


 

                <div className="col-md-6">

                  <label className="small fw-bold text-muted mb-2">ASSIGN DOCTOR</label>

                  <select className="form-select form-control-rec" name="doctor" value={formData.doctor} onChange={handleInputChange} required disabled={!selectedDept}>

                    <option value="">Select Doctor</option>

                    {doctors.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}

                  </select>

                </div>

              </div>


 

              <button type="submit"

              disabled={!isFormValid}

               className="btn w-100 py-3 mt-4 text-white fw-bold shadow border-0"

               style={{ background: !isFormValid?'#adb5bd':'#f2994a',

                      borderRadius: '12px',

                      cursor:!isFormValid?'not-allowed':'pointer'

                }}>GENERATE TOKEN</button>

            </form>

          </div>

        )}

        {/* TODAY'S APPOINTMENTS */}


 

        {activeTab === 'appointments' && (


 

          <div className="glass-card-rec p-4">


 

            <h4 className="fw-bold mb-4">Today's Bookings</h4>


 

            <div className="table-responsive">


 

              <table className="table table-rec align-middle">


 

                <thead><tr><th>Time</th><th>Patient</th><th>Doctor</th><th>Reason</th><th>Action</th></tr></thead>


 

                <tbody>


 

                  {appointments.map((a: any) => (


 

                    <tr key={a.id}>


 

                      <td><span className="fw-bold">{formatTime(a.createdAt)}</span></td>


 

                      <td><div className="fw-bold">{a.patientName}</div><small className="text-muted">{a.contact}</small></td>


 

                      <td>{a.doctorName}</td>


 

                      <td className="small text-muted">{a.reason || 'General'}</td>


 

                      <td>


 

                        {a.status !== 'CHECKED_IN' ? (


 

                          <button className="btn btn-sm text-white px-3 fw-bold" style={{ background: '#f2994a', borderRadius: '8px' }} onClick={() => handleCheckIn(a.id)}>CHECK IN</button>


 

                        ) : <span className="badge bg-success-subtle text-success px-3">Checked In</span>}


 

                      </td>


 

                    </tr>


 

                  ))}


 

                </tbody>


 

              </table>


 

            </div>


 

          </div>


 

        )}





 

        {/* SEARCH PATIENTS */}


 

        {activeTab === 'search' && (


 

          <div className="glass-card-rec p-5 animate-in">


 

            <h3 className="fw-bold mb-4">Patient Registry</h3>


 

            <div className="input-group mb-5 shadow-sm rounded-pill overflow-hidden border">


 

              <input type="text" className="form-control border-0 px-4 py-3" placeholder="Search by patient name or phone..." value={searchKeyword} onChange={(e) => setSearchKeyword(e.target.value)} />


 

              <button className="btn px-5 fw-bold text-white border-0" style={{ background: '#f2994a' }} onClick={handleSearch}>SEARCH</button>


 

            </div>


 

            {showSearchResults && (


 

              <div className="table-responsive">


 

                <table className="table table-rec align-middle">


 

                  <thead><tr><th>Name</th><th>Contact</th><th>Age</th><th className="text-end">Actions</th></tr></thead>


 

                  <tbody>


 

                    {searchResults.map((item: any) => (


 

                      <tr key={item.id}>


 

                        <td className="fw-bold">{item.patientName || item.name}</td>


 

                        <td className="text-muted">{item.phone || item.contact}</td>


 

                        <td>{calculateAge(item.dateOfBirth)}</td>


 

                        <td className="text-end">


 

                          <div className="d-flex gap-2 justify-content-end">


 

                            <button


 

                              className="btn btn-sm btn-dark rounded-pill px-3 fw-bold shadow-sm"


 

                              onClick={() => handleViewPatientAppointments(item.id, item.patientName || item.name)}


 

                            >


 

                              History


 

                            </button>


 

                            <button


 

                              className="btn btn-sm btn-outline-warning rounded-pill px-3 fw-bold shadow-sm"


 

                              onClick={() => {


 

                                setFormData({ ...formData, name: item.patientName || item.name, contact: item.phone || item.contact, dob: item.dateOfBirth, email: '', password: '', gender: 'MALE', doctor: '', reason: '' });


 

                                setActiveTab('walk-in');


 

                              }}


 

                            >


 

                              New Visit


 

                            </button>


 

                          </div>


 

                        </td>


 

                      </tr>


 

                    ))}


 

                  </tbody>


 

                </table>


 

              </div>


 

            )}


 

          </div>


 

        )}





 

        {/* Global Live Queue Footer */}


 

        <div className="glass-card-rec p-4 mt-5">


 

          <h5 className="fw-bold mb-4"><span style={{ color: '#f2994a' }}>Live</span> Global Queue</h5>


 

          <div className="table-responsive">


 

            <table className="table table-rec align-middle mb-0">


 

              <thead><tr><th>Token</th><th>Patient Identity</th><th>Assigned Dr.</th><th>Arrival</th><th>Status</th></tr></thead>


 

              <tbody>


 

                {patients.length > 0 ? patients.map((t: any) => (


 

                  <tr key={t.id}>


 

                    <td className="fw-bold" style={{ color: '#f2994a' }}>#{t.tokenNumber}</td>


 

                    <td><div className="fw-bold">{t.patient?.name}</div><small className="text-muted">{t.patient?.phone}</small></td>


 

                    <td>{t.doctor?.name}</td>


 

                    <td>{formatTime(t.checkInTime)}</td>


 

                    <td><span className={`badge rounded-pill px-3 py-1 ${t.status === 'WAITING' ? 'bg-warning-subtle text-warning' : 'bg-success-subtle text-success'} border`} style={{ fontSize: '9px' }}>{t.status}</span></td>


 

                  </tr>


 

                )) : <tr><td colSpan={5} className="text-center py-4 text-muted italic">No active tokens.</td></tr>}


 

              </tbody>


 

            </table>


 

          </div>


 

        </div>


 

      </div>


 

    </div>


 

  );


 

};





 

export default ReceptionistDashboard;