// import axios from 'axios';

// import React, { useEffect, useState, useCallback } from 'react';

// import Swal from 'sweetalert2';

// import Sidebar from '../../layout/Sidebar';

// import axiosInstance from '../../../services/axiosInstance';

// // At the top of AdminDashboard.tsx, update your props interface

// interface AdminDashboardProps {

//     onToggleForm?: (isOpen: boolean) => void;

// }


 

// // US 11

// interface Doctor {

//     id: number;

//     name: string;

//     specialization: string;

//     days: string[];

//     timeStart: string;

//     timeEnd: string;

//     slot: string;

// }



 

// const AdminDashboard: React.FC<AdminDashboardProps> = ({ onToggleForm }) => {

//     const [activeTab, setActiveTab] = useState<'scheduling' | 'rules' | 'analytics'>('scheduling');

//     // 2. Change your state to start empty

//     const [doctors, setDoctors] = useState<Doctor[]>([]);

//     //list of doctors



 

//     const [showForm, setShowForm] = useState(false);

//     const [editingDoctor, setEditingDoctor] = useState<Doctor | null>(null);


 

//     const allDays = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

//     const [selectedDays, setSelectedDays] = useState<string[]>([]);

//     const [maxPatients, setMaxPatients] = useState(1);

//     const [maxDays, setMaxDays] = useState(30);

//     const [cutoffHours, setCutoffHours] = useState(2);

//     const handleUpdateRules = async () => {

//         try {


 

//             const response = await axiosInstance.post('/clinicq/admin/booking-rules',null, {

//                 params: {

//                     maxPatients: maxPatients,

//                     maxDays: maxDays,

//                     cutoffHours: cutoffHours

//                 }


 

//             });


 

//             if (response.status === 200) {

//                 Swal.fire({

//                     title: 'Rules Updated!',

//                     text: response.data, // Shows "Booking rules updated successfully"

//                     icon: 'success',

//                     background: '#111',

//                     color: '#fff',

//                     confirmButtonColor: '#ffc107',

//                     iconColor: '#ffc107'

//                 });

//             }

//         } catch (error: any) {

//             console.error("Error Updating Rules:", error);

//             // If you get here, check if your Spring Boot console shows any errors

//             Swal.fire({

//                 title: 'Connection Failed',

//                 text: 'Could not reach the server. Make sure CORS is enabled in Java.',

//                 icon: 'error',

//                 background: '#111',

//                 color: '#fff',

//                 confirmButtonColor: '#dc3545'

//             });

//         }

//     };

//     // US 11: SAVE / UPDATE DOCTOR

//     const handleSaveDoctor = async (e: React.FormEvent<HTMLFormElement>) => {

//         e.preventDefault();

//         const formData = new FormData(e.currentTarget);


 

//         const dayMap: { [key: string]: string } = {

//             'Mon': 'MONDAY', 'Tue': 'TUESDAY', 'Wed': 'WEDNESDAY',

//             'Thu': 'THURSDAY', 'Fri': 'FRIDAY', 'Sat': 'SATURDAY', 'Sun': 'SUNDAY'

//         };


 

//         // Join the selected days into a comma-separated string: "MONDAY,TUESDAY"

//         const selectedDaysList = selectedDays.map(d => dayMap[d]).join(',');


 

//         try {

//             if (editingDoctor) {

//                 // --- UPDATE EXISTING DOCTOR SCHEDULE ---

//                 const dayMap: { [key: string]: string } = {

//                     'Mon': 'MONDAY', 'Tue': 'TUESDAY', 'Wed': 'WEDNESDAY',

//                     'Thu': 'THURSDAY', 'Fri': 'FRIDAY', 'Sat': 'SATURDAY', 'Sun': 'SUNDAY'

//                 };

//                 const selectedDaysList = selectedDays.map(d => dayMap[d]).join(',');

//                 // We loop here because we might be updating multiple days

//                 const token = localStorage.getItem('token');

//                 const response = await axiosInstance.put( // Changed from .post to .put

//                     `/clinicq/admin/doctor/${editingDoctor.id}`,

//                     null,

//                     {


 

//                         params: {

//                             days: selectedDaysList, // Sending the full array string

//                             startTime: formData.get('timeStart'),

//                             endTime: formData.get('timeEnd'),

//                             slotDuration: parseInt((formData.get('slot') as string).split(' ')[0])

//                         }

//                     }

//                 );

//                 if (response.status === 200) {

//                     Swal.fire({ title: 'Updated!', text: 'Schedule configured successfully', icon: 'success', background: '#111', color: '#fff' });

//                 }

//                 //Swal.fire({ title: 'Updated!', text: 'Schedules updated successfully', icon: 'success', background: '#111', color: '#fff' });



 

//             } else {

//                 const token = localStorage.getItem('token');

//                 const response = await axiosInstance.post(

//                     `/clinicq/admin/doctor`,

//                     null,

//                     {


 

//                         params: {

//                             email: formData.get('email'),

//                             password: formData.get('password'),

//                             name: formData.get('name'),

//                             department: formData.get('department'),

//                             gender: formData.get('gender'),

//                             phone: formData.get('phone'),

//                             location: formData.get('location'),

//                             description: formData.get('description'),

//                             days: selectedDaysList, // Send the WHOLE list here

//                             startTime: formData.get('timeStart'),

//                             endTime: formData.get('timeEnd'),

//                             slotDuration: parseInt((formData.get('slot') as string).split(' ')[0])

//                         }

//                     }

//                 );

//                 Swal.fire({ title: 'Registered!', text: 'Doctor registered with selected schedules', icon: 'success', background: '#111', color: '#fff' });

//             }


 

//             // --- SUCCESS CLEANUP (Outside the loops, inside the try) ---

//             setShowForm(false);

//             if (onToggleForm) onToggleForm(false);

//             setEditingDoctor(null);

//             setSelectedDays([]);

//             fetchDoctors();


 

//         } catch (err: any) {

//             console.error("API Error:", err);

//             Swal.fire({

//                 icon: 'error',

//                 title: 'Action Failed',

//                 text: err.response?.data?.errorMessage || "Check your data (Unique Email & 10-digit Phone)",

//                 background: '#111', color: '#fff'

//             });

//         }

//     };


 

//     const formatDays = (days: string[]) => {

//         const order = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];


 

//         // Sort days based on the actual week order

//         const sortedDays = days.sort((a, b) => order.indexOf(a) - order.indexOf(b));


 

//         if (sortedDays.length === 5 && sortedDays[0] === "MON" && sortedDays[4] === "FRI") {

//             return ["Mon-Fri"];

//         }

//         else if (sortedDays.length === 7 && sortedDays[0] === "MON" && sortedDays[6] === "SUN") {

//             return ["Mon-Sun"];

//         }


 

//         // Capitalize only first letter for better UI (Mon, Tue, Wed)

//         return sortedDays.map(d => d.charAt(0) + d.slice(1).toLowerCase());

//     };

//     // 3. Add the Fetch Function


 

//     const fetchDoctors = useCallback(async () => {

//         try {

//             // Replace with your actual Admin API endpoint to get all doctors

//             const token = localStorage.getItem('token');

//             const response = await axiosInstance.get("/clinicq/admin/doctors");


 

//             // 1. Use a Map to group by Doctor ID

//             const grouped = new Map<number, any>();


 

//             response.data.forEach((item: any) => {

//                 const docId = item.doctor.id;

//                 const day = item.dayOfWeek.substring(0, 3); // Get "MON", "TUE", etc.


 

//                 if (grouped.has(docId)) {

//                     // Doctor already exists, just add the day to the array

//                     const existing = grouped.get(docId);

//                     if (!existing.days.includes(day)) {

//                         existing.days.push(day);

//                     }

//                 } else {

//                     // New doctor entry

//                     grouped.set(docId, {

//                         id: docId,

//                         name: item.doctor.name,

//                         specialization: item.doctor.department,

//                         days: [day], // Start a new list of days

//                         timeStart: item.startTime.substring(0, 5),

//                         timeEnd: item.endTime.substring(0, 5),

//                         slot: `${item.slotDuration} mins`

//                     });

//                 }

//             });


 

//             // 2. Convert Map back to an array and format the day strings

//             const formattedDoctors = Array.from(grouped.values()).map(doc => {

//                 return {

//                     ...doc,

//                     days: formatDays(doc.days) // Call a helper function to handle "Mon-Fri"

//                 };

//             });


 

//             setDoctors(formattedDoctors);

//         } catch (err) {

//             console.error("Error fetching doctors:", err);

//         }

//     }, []);




 

//     const handleDeleteDoctor = (id: number) => {

//         // 1. Show Confirmation Dialog

//         Swal.fire({

//             title: 'Are you sure?',

//             text: "This doctor and their schedule will be permanently removed!",

//             icon: 'warning',

//             showCancelButton: true,

//             confirmButtonColor: '#0dcaf0', // matching your text-info color

//             cancelButtonColor: '#d33',

//             confirmButtonText: 'Yes, delete it!',

//             background: '#111',

//             color: '#fff'

//         }).then(async (result) => {

//             if (result.isConfirmed) {

//                 try {

//                     // 2. Call the Backend API

//                     const token = localStorage.getItem('token');

//                     const response = await axiosInstance.delete(`/clinicq/admin/doctor/${id}`);


 

//                     if (response.status === 200 || response.status === 204) {

//                         // 3. Show Success Message

//                         Swal.fire({

//                             title: 'Deleted!',

//                             text: 'Doctor has been removed.',

//                             icon: 'success',

//                             background: '#111',

//                             color: '#fff'

//                         });


 

//                         // 4. Refresh the UI table

//                         fetchDoctors();

//                     }

//                 } catch (err: any) {

//                     console.error("Delete Error:", err);

//                     Swal.fire({

//                         title: 'Error!',

//                         text: err.response?.data?.errorMessage || 'Could not delete the doctor.',

//                         icon: 'error',

//                         background: '#111',

//                         color: '#fff'

//                     });

//                 }

//             }

//         });

//     };


 

//     const openForm = (doc: Doctor | null) => {

//         setEditingDoctor(doc);

//         setSelectedDays(doc ? doc.days : []);

//         setShowForm(true);

//         if (onToggleForm) onToggleForm(true); // Signal that form is OPEN

//     };

//     // Update the part where you close the form (Cancel button or Save success)

//     const closeForm = () => {

//         setShowForm(false);

//         if (onToggleForm) onToggleForm(false); // Signal that form is CLOSED

//     };


 

//     useEffect(() => {

//         fetchDoctors();

//     }, [fetchDoctors]); // fetchDoctors is now a stable dependency


 

//     return (

//         <div className="d-flex bg-black text-white min-vh-100">

//             <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />

//             <div className="flex-grow-1 p-4 overflow-auto">


 

//                 {/* US 11 : DOCTOR SCHEDULING */}

//                 {activeTab === 'scheduling' && (

//                     <div className="animate-fade-in">

//                         <h2 className="text-info border-bottom border-info pb-3 mb-4 fw-bold">Doctor Scheduling</h2>


 

//                         <div className="table-responsive rounded shadow-lg mb-4">

//                             <table className="table table-dark table-hover align-middle border-info">

//                                 <thead className="table-secondary text-info">

//                                     <tr>

//                                         <th>Doctor Name</th>

//                                         <th>Department</th>

//                                         <th>Working Days</th>

//                                         <th>Working Hours</th>

//                                         <th>Slot Duration</th>

//                                         <th>Actions</th>

//                                     </tr>

//                                 </thead>

//                                 <tbody>

//                                     {doctors.length > 0 ? (

//                                         doctors.map(doc => (

//                                             <tr key={doc.id}>

//                                                 <td className="fw-bold">{doc.name}</td>

//                                                 <td><span className="badge bg-secondary">{doc.specialization}</span></td>

//                                                 <td>{doc.days.join(", ")}</td>

//                                                 <td>{doc.timeStart} - {doc.timeEnd}</td>

//                                                 <td className="text-info fw-bold">{doc.slot}</td>

//                                                 <td>

//                                                     <div className="d-flex gap-2">

//                                                         <button className="btn btn-sm btn-outline-warning fw-bold" onClick={() => openForm(doc)}>Modify</button>

//                                                         <button className="btn btn-sm btn-outline-danger fw-bold" onClick={() => handleDeleteDoctor(doc.id)}>Delete</button>

//                                                     </div>

//                                                 </td>

//                                             </tr>

//                                         ))

//                                     ) : (

//                                         <tr>

//                                             <td colSpan={6} className="text-center py-4 text-secondary italic">

//                                                 No doctors registered yet. Click "+ Add New Doctor" to begin.

//                                             </td>

//                                         </tr>

//                                     )}

//                                 </tbody>

//                             </table>

//                         </div>


 

//                         <button className="btn btn-info fw-bold px-4 shadow" onClick={() => openForm(null)}>+ Add New Doctor</button>


 

//                         {showForm && (

//                             <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center"

//                                 style={{ backgroundColor: 'rgba(0,0,0,0.85)', zIndex: 1050 }}>

//                                 <div className="card bg-dark border-info p-4 shadow-lg" style={{

//                                     width: '500px',

//                                     maxHeight: '90vh',

//                                     overflowY: 'auto',

//                                     scrollbarWidth: 'thin', /* Makes scrollbar less intrusive */

//                                     scrollbarColor: '#0dcaf0 #212529'

//                                 }}>

//                                     <h3 className="text-info mb-4 fw-bold">{editingDoctor ? `Modify Settings` : 'Register New Doctor'}</h3>

//                                     <form onSubmit={handleSaveDoctor}>

//                                         {!editingDoctor && (

//                                             <div className="mb-3">

//                                                 <label className="small text-info fw-bold">Doctor Details</label>

//                                                 {/* Row 1: Name & Email */}

//                                                 <div className="row g-2 mb-2">

//                                                     <div className="col-6">

//                                                         <input name="name" placeholder="Doctor Name" className="form-control bg-black text-white border-secondary shadow-none" required />

//                                                     </div>

//                                                     <div className="col-6">

//                                                         <input name="email" type="email" placeholder="Email Address" className="form-control bg-black text-white border-secondary shadow-none" required />

//                                                     </div>

//                                                 </div>


 

//                                                 {/* Row 2: Password & Phone */}

//                                                 <div className="row g-2 mb-2">

//                                                     <div className="col-6">

//                                                         <input name="password" type="password" placeholder="Password" className="form-control bg-black text-white border-secondary shadow-none" required />

//                                                     </div>

//                                                     <div className="col-6">

//                                                         <input name="phone" placeholder="Phone Number" className="form-control bg-black text-white border-secondary shadow-none" required />

//                                                     </div>

//                                                 </div>


 

//                                                 {/* Row 3: Department & Gender (Dropdowns) */}

//                                                 <div className="row g-2 mb-2">

//                                                     <div className="col-6">

//                                                         <select name="department" className="form-select bg-black text-white border-secondary shadow-none" required>

//                                                             <option value="" disabled hidden>Department</option>

//                                                             <option value="GENERAL">GENERAL</option>

//                                                             <option value="CARDIOLOGY">CARDIOLOGY</option>

//                                                             <option value="ORTHOPEDICS">ORTHOPEDICS</option>

//                                                             <option value="PEDIATRICS">PEDIATRICS</option>

//                                                         </select>

//                                                     </div>

//                                                     <div className="col-6">

//                                                         <select name="gender" className="form-select bg-black text-white border-secondary shadow-none" required>

//                                                             <option value="" disabled hidden>Gender</option>

//                                                             <option value="MALE">MALE</option>

//                                                             <option value="FEMALE">FEMALE</option>

//                                                             <option value="OTHER">OTHER</option>

//                                                         </select>

//                                                     </div>

//                                                 </div>


 

//                                                 {/* Row 4: Location & Specialization */}

//                                                 <div className="mb-2">


 

//                                                     <input name="location" placeholder="Clinic Location" className="form-control bg-black text-white border-secondary shadow-none" required />


 

//                                                 </div>


 

//                                                 {/* Description Field */}

//                                                 <div className="mb-2">

//                                                     <textarea name="description" placeholder="Brief Description/Bio" className="form-control bg-black text-white border-secondary shadow-none" rows={2} required></textarea>

//                                                 </div>

//                                             </div>

//                                         )}

//                                         <div className="mb-3">

//                                             <label className="small text-info fw-bold">Select Working Days</label>

//                                             <div className="d-flex flex-wrap gap-2 mt-1">

//                                                 {allDays.map(day => (

//                                                     <div key={day} className="form-check">

//                                                         <input type="checkbox" className="btn-check" id={`btn-${day}`} checked={selectedDays.includes(day)}

//                                                             onChange={(e) => {

//                                                                 if (e.target.checked) setSelectedDays([...selectedDays, day]);

//                                                                 else setSelectedDays(selectedDays.filter(d => d !== day));

//                                                             }}

//                                                         />

//                                                         <label className={`btn btn-sm ${selectedDays.includes(day) ? 'btn-info' : 'btn-outline-secondary'}`}

//                                                             htmlFor={`btn-${day}`}>{day}</label>

//                                                     </div>


 

//                                                 ))}

//                                             </div>

//                                         </div>

//                                         <div className="row mb-3">

//                                             <div className="col">

//                                                 <label className="small text-info fw-bold">Start Time</label>

//                                                 <input type="time" name="timeStart" className="form-control bg-black text-white border-secondary" required

//                                                     defaultValue={editingDoctor?.timeStart || "09:00"} />

//                                             </div>

//                                             <div className="col">

//                                                 <label className="small text-info fw-bold">End Time</label>

//                                                 <input type="time" name="timeEnd" className="form-control bg-black text-white border-secondary" required

//                                                     defaultValue={editingDoctor?.timeEnd || "17:00"} />

//                                             </div>

//                                         </div>

//                                         <div className="mb-4">

//                                             <label className="small text-info fw-bold">Slot Length</label>

//                                             <select name="slot" className="form-select bg-black text-white border-secondary" defaultValue={editingDoctor?.slot || "15 mins"}>

//                                                 <option value="10 mins">10 mins</option>

//                                                 <option value="15 mins">15 mins</option>

//                                                 <option value="20 mins">20 mins</option>

//                                             </select>

//                                         </div>

//                                         <div className="d-flex gap-3 pb-2">

//                                             <button type="submit" className="btn btn-info flex-grow-1 fw-bold text-black">Save</button>

//                                             <button type="button" className="btn btn-outline-danger" onClick={() => {

//                                                 setShowForm(false);

//                                                 if (onToggleForm) onToggleForm(false);

//                                             }}>Cancel</button>

//                                         </div>

//                                     </form>

//                                 </div>

//                             </div>


 

//                         )}

//                     </div>


 

//                 )}


 

//                 {/* US 12: CLINIC RULES */}

//                 {activeTab === 'rules' && (

//                     <div className="animate-fade-in">

//                         <h2 className="text-warning border-bottom border-warning pb-3 mb-4 fw-bold">Clinic Booking Rules</h2>

//                         <div className="card bg-dark border-warning p-4 col-lg-6 shadow-lg">

//                             <div className="mb-3">

//                                 <label className="form-label text-warning fw-bold">Max Appointments Per Slot</label>

//                                 <input type="number" className="form-control bg-black text-white border-warning py-2" value={maxPatients}

//                                     onChange={(e) => setMaxPatients(parseInt(e.target.value) || 0)

//                                     } min={1} />


 

//                             </div>

//                             <div className="mb-3">

//                                 <label className="form-label text-warning fw-bold">Maximum Days in Advance</label>

//                                 <div className="input-group">

//                                     <input type="number" className="form-control bg-black text-white border-warning py-2" value={maxDays}

//                                         onChange={(e) => setMaxDays(parseInt(e.target.value))} min={1} />

//                                     <span className="input-group-text bg-warning text-black fw-bold border-warning">Days</span>

//                                 </div>

//                                 <small className="text-secondary">Patients can only book up to this many days ahead.</small>

//                             </div>

//                             <div className="mb-3">

//                                 <label className="form-label text-warning fw-bold">Cancellation Window (Hours)</label>

//                                 <div className="input-group">

//                                     <input type="number" className="form-control bg-black text-white border-warning py-2" value={cutoffHours}

//                                         onChange={(e) => setCutoffHours(parseInt(e.target.value))} min={1} />

//                                     <span className="input-group-text bg-warning text-black fw-bold border-warning">Hours</span>

//                                 </div>

//                             </div>

//                             <button className="btn btn-warning fw-bold mt-2 py-2" onClick={handleUpdateRules}>Save Rules</button>


 

//                         </div>

//                     </div>

//                 )}


 

//                 {/* US 14: ANALYTICS */}

//                 {activeTab === 'analytics' && (

//                     <div className="animate-fade-in">

//                         <h2 className="text-success border-bottom border-success pb-3 mb-4 fw-bold">Clinic Analytics</h2>

//                         <div className="row g-3 mb-4">

//                             <div className="col-12 col-md-4">

//                                 <label className="small text-success fw-bold">Select Date Range</label>

//                                 <input type="date" className="form-control bg-dark text-white border-success" defaultValue="2026-04-20" />

//                             </div>

//                             <div className="col-12 col-md-4">

//                                 <label className="small text-success fw-bold">Filter by Doctor</label>

//                                 <select className="form-select bg-dark text-white border-success">

//                                     <option value="all">All Doctors</option>

//                                     <option value="1">Dr. Smith</option>

//                                     <option value="2">Dr. Rao</option>

//                                 </select>

//                             </div>

//                         </div>

//                         <div className="row g-4 mb-5">

//                             {[

//                                 { label: 'Total Booked', val: 45, col: 'info' },

//                                 { label: 'Walk-ins', val: 12, col: 'success' },

//                                 { label: 'Completed', val: 50, col: 'warning' },

//                                 { label: 'No-Shows', val: 7, col: 'danger' }


 

//                             ].map((s, i) => (

//                                 <div key={i} className="col-6 col-md-3">

//                                     <div className={`card bg-dark border-0 border-top border-4 border-${s.col} p-3 shadow-lg h-100`}>

//                                         <h6 className="text-secondary small fw-bold">{s.label}</h6>

//                                         <h2 className={`text-${s.col} fw-bold`}>{s.val}</h2>

//                                     </div>

//                                 </div>

//                             ))}

//                         </div>

//                         <div className="row g-4">

//                             <div className="col-12 col-lg-6">

//                                 <div className="card bg-dark border-secondary p-4 h-100">

//                                     <h5 className="text-info mb-4 fw-bold">Doctor Performance Breakdown</h5>

//                                     <div className="list-group list-group-flush">

//                                         <div className="list-group-item bg-transparent text-white d-flex justify-content-between border-secondary">

//                                             <span>Dr. Smith</span>

//                                             <span className="badge bg-info">20 Patients</span>

//                                         </div>

//                                         <div className="list-group-item bg-transparent text-white d-flex justify-content-between border-secondary">

//                                             <span>Dr. Rahul</span>

//                                             <span className="badge bg-info">12 Patients</span>

//                                         </div>

//                                     </div>

//                                 </div>

//                             </div>

//                             <div className="col-12 col-lg-6">

//                                 <div className="card bg-dark border-secondary p-4 h-100">

//                                     <h5 className="text-success mb-4 fw-bold">Daily Patient Count</h5>


 

//                                     {/* CHART COMPONENT */}

//                                     <div className="position-relative" style={{ height: '150px', marginTop: '20px' }}>

//                                         <svg viewBox="0 0 700 150" className="w-100 h-100">

//                                             <polyline fill="none" stroke="#198754" strokeWidth="3" points="50,120 150,90 250,50 350,100 450,40 550,80 650,60" />

//                                             {[

//                                                 { x: 50, y: 120, val: 30 }, { x: 150, y: 90, val: 50 }, { x: 250, y: 50, val: 80 },

//                                                 { x: 350, y: 100, val: 40 }, { x: 450, y: 40, val: 90 }, { x: 550, y: 80, val: 60 }, { x: 650, y: 60, val: 75 }

//                                             ].map((p, i) => (

//                                                 <g key={i}>

//                                                     <circle cx={p.x} cy={p.y} r="5" fill="#198754" />

//                                                     <text x={p.x} y={p.y - 15} fill="#20c997" fontSize="14" textAnchor="middle" fontWeight="bold">{p.val}</text>

//                                                 </g>

//                                             ))}

//                                         </svg>

//                                     </div>

//                                     <div className="d-flex justify-content-between mt-3 text-muted small px-4">

//                                         <span>Mon</span><span>Tue</span><span>Wed</span><span>Thu</span><span>Fri</span><span>Sat</span><span>Sun</span>

//                                     </div>

//                                 </div>

//                             </div>

//                         </div>

//                     </div>

//                 )}




 

//             </div>

//         </div>


 

//     );

// };


 

// export default AdminDashboard;

import axios from 'axios';


 

import React, { useEffect, useState, useCallback } from 'react';


 

import Swal from 'sweetalert2';


 

import Sidebar from '../../layout/Sidebar';


 

import axiosInstance from '../../../services/axiosInstance';





 

interface AdminDashboardProps {


 

    onToggleForm?: (isOpen: boolean) => void;


 

}





 

interface Doctor {


 

    id: number;


 

    name: string;


 

    specialization: string;


 

    days: string[];


 

    timeStart: string;


 

    timeEnd: string;


 

    slot: string;


 

}





 

const AdminDashboard: React.FC<AdminDashboardProps> = ({ onToggleForm }) => {


 

    const [activeTab, setActiveTab] = useState<'scheduling' | 'rules' | 'analytics'>('scheduling');


 

    const [doctors, setDoctors] = useState<Doctor[]>([]);


 

    const [showForm, setShowForm] = useState(false);


 

    const [editingDoctor, setEditingDoctor] = useState<Doctor | null>(null);





 

    const allDays = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];


 

    const [selectedDays, setSelectedDays] = useState<string[]>([]);


 

    const [maxPatients, setMaxPatients] = useState(1);


 

    const [maxDays, setMaxDays] = useState(30);


 

    const [cutoffHours, setCutoffHours] = useState(2);



 

    const [isFormValid, setIsFormValid] = useState<boolean>(false);


 

    // Validation Patterns

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    const phoneRegex = /^[0-9]{10}$/;

    const passRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;


 

    const [formInputs, setFormInputs] = useState({

        name: "",

        email: "",

        password: "",

        phone: "",

        location: "",

        description: ""

    });

    const handleFieldChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {

        const{name,value}=e.target;

        setFormInputs(prev=>({ ...prev, [name]: value }));

    }

    // --- FUNCTIONALITY (STRICTLY UNTOUCHED) ---


 

    const handleUpdateRules = async () => {


 

        try {


 

            const response = await axiosInstance.post('/clinicq/admin/booking-rules', null, {


 

                params: { maxPatients, maxDays, cutoffHours }


 

            });


 

            if (response.status === 200) {


 

                Swal.fire({


 

                    title: 'Rules Updated!', text: response.data, icon: 'success',


 

                    background: '#fff', color: '#111', confirmButtonColor: '#ff7e5f'


 

                });


 

            }


 

        } catch (error: any) {


 

            Swal.fire({ title: 'Connection Failed', text: 'Check server connection.', icon: 'error', background: '#fff', color: '#111' });


 

        }


 

    };





 

    const handleSaveDoctor = async (e: React.FormEvent<HTMLFormElement>) => {


 

        e.preventDefault();


 

        const formData = new FormData(e.currentTarget);


 

        const dayMap: { [key: string]: string } = { 'Mon': 'MONDAY', 'Tue': 'TUESDAY', 'Wed': 'WEDNESDAY', 'Thu': 'THURSDAY', 'Fri': 'FRIDAY', 'Sat': 'SATURDAY', 'Sun': 'SUNDAY' };


 

        const selectedDaysList = selectedDays.map(d => dayMap[d]).join(',');





 

        try {


 

            if (editingDoctor) {


 

                await axiosInstance.put(`/clinicq/admin/doctor/${editingDoctor.id}`, null, {


 

                    params: {


 

                        days: selectedDaysList,


 

                        startTime: formData.get('timeStart'),


 

                        endTime: formData.get('timeEnd'),


 

                        slotDuration: parseInt((formData.get('slot') as string).split(' ')[0])


 

                    }


 

                });


 

                Swal.fire({ title: 'Updated!', text: 'Schedule configured', icon: 'success', confirmButtonColor: '#ff7e5f' });


 

            } else {

                const rawName = formData.get('name') as string;

                //const rawSlot=formData.get('slot') as string;

                const finalName = rawName.startsWith("Dr. ") ? rawName : `Dr. ${rawName}`;

                //const finalSlot=rawSlot?parseInt(rawSlot.split(' ')[0]):15;

                await axiosInstance.post(`/clinicq/admin/doctor`, null, {

                    params: {

                        email: formData.get('email'),

                        password: formData.get('password'),

                        name: finalName,

                        department: formData.get('department'),

                        gender: formData.get('gender'),

                        phone: formData.get('phone'),

                        location: formData.get('location'),

                        description: formData.get('description'),

                        days: selectedDaysList,

                        startTime: formData.get('timeStart'),

                        endTime: formData.get('timeEnd'),

                        slotDuration: parseInt((formData.get('slot') as string).split(' ')[0])

                    }


 

                });


 

                Swal.fire({ title: 'Registered!', text: 'Doctor added successfully.', icon: 'success', confirmButtonColor: '#ff7e5f' });


 

            }


 

            setShowForm(false);

            if (onToggleForm) onToggleForm(false);

            setEditingDoctor(null);

            setSelectedDays([]);

            fetchDoctors();


 

        } catch (err: any) {

            Swal.fire({

                icon: 'error',

                title: 'Action Failed',

                text: 'Verify if rules are set',

                background: '#fff',

                color: '#111' });


 

        }


 

    };





 

    const formatDays = (days: string[]) => {


 

        const order = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];


 

        const sortedDays = days.sort((a, b) => order.indexOf(a) - order.indexOf(b));


 

        if (sortedDays.length === 5 && sortedDays[0] === "MON" && sortedDays[4] === "FRI") return ["Mon-Fri"];


 

        if (sortedDays.length === 7 && sortedDays[0] === "MON" && sortedDays[6] === "SUN") return ["Mon-Sun"];


 

        return sortedDays.map(d => d.charAt(0) + d.slice(1).toLowerCase());


 

    };





 

    const fetchDoctors = useCallback(async () => {


 

        try {


 

            const response = await axiosInstance.get("/clinicq/admin/doctors");


 

            const grouped = new Map<number, any>();


 

            response.data.forEach((item: any) => {


 

                const docId = item.doctor.id;


 

                const day = item.dayOfWeek.substring(0, 3);


 

                if (grouped.has(docId)) {


 

                    const existing = grouped.get(docId);


 

                    if (!existing.days.includes(day)) existing.days.push(day);


 

                } else {


 

                    grouped.set(docId, {


 

                        id: docId, name: item.doctor.name, specialization: item.doctor.department,


 

                        days: [day], timeStart: item.startTime.substring(0, 5), timeEnd: item.endTime.substring(0, 5), slot: `${item.slotDuration} mins`


 

                    });


 

                }


 

            });


 

            const formattedDoctors = Array.from(grouped.values()).map(doc => ({ ...doc, days: formatDays(doc.days) }));


 

            setDoctors(formattedDoctors);


 

        } catch (err) { console.error(err); }


 

    }, []);





 

    const handleDeleteDoctor = (id: number) => {


 

        Swal.fire({


 

            title: 'Are you sure?', text: "Doctor will be removed permanently!", icon: 'warning',


 

            showCancelButton: true, confirmButtonColor: '#ff6b6b', cancelButtonColor: '#adb5bd',


 

            confirmButtonText: 'Delete', background: '#fff', color: '#111'


 

        }).then(async (result) => {


 

            if (result.isConfirmed) {


 

                try {


 

                    await axiosInstance.delete(`/clinicq/admin/doctor/${id}`);

                    setDoctors(prevDoctors=>prevDoctors.filter(doc=>doc.id!==id));


 

                    await Swal.fire({

                        title:'Deleted!',

                        text:'Doctor removed successfully.',

                        icon:'success',

                        timer:1500,

                        showConfirmButton:false

                    });


 

                    fetchDoctors();


 

                } catch (err) { Swal.fire('Error', 'Delete failed. Please try again.', 'error'); }


 

            }


 

        });


 

    };





 

    const openForm = (doc: Doctor | null) => {


 

        setEditingDoctor(doc);


 

        setSelectedDays(doc ? doc.days : []);


 

        setShowForm(true);


 

        if (onToggleForm) onToggleForm(true);


 

    };





 

    const closeForm = () => {


 

        setShowForm(false);

        setFormInputs({ name: "", email: "", password: "", phone: "", location: "", description: "" });

        setEditingDoctor(null);

        setSelectedDays([]);


 

        if (onToggleForm) onToggleForm(false);


 

    };





 

    useEffect(() => { fetchDoctors(); }, [fetchDoctors]);

    // Effect to monitor form completion in real-time

    useEffect(() => {

        const { name, email, password, phone, location, description } = formInputs;

        // If we are editing, we only need to check days and times

        if (editingDoctor) {

            setIsFormValid(selectedDays.length > 0);

            return;

        }


 

        const isNameOk = /^[a-zA-Z\S]{2,50}$/.test(name);

        const isEmailOk = emailRegex.test(email);

        const isPassOk = passRegex.test(password);

        const isPhoneOk = phoneRegex.test(phone);

        const isOtherOk = formInputs.location.trim().length>=2 && formInputs.description.trim().length >= 10;

        const isDaysOk = selectedDays.length > 0;


 

        // We call this whenever selectedDays or the form state might change

        setIsFormValid(isNameOk && isEmailOk && isPassOk && isPhoneOk && isOtherOk && isDaysOk);

    }, [formInputs, selectedDays, editingDoctor]);




 

    return (


 

        <div className="d-flex min-vh-100" style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>


 

            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />





 

            <div className="flex-grow-1 p-4 overflow-auto">


 

                <style>


 

                    {`


 

                    .glass-panel {


 

                        background: rgba(255, 255, 255, 0.7);


 

                        backdrop-filter: blur(25px);


 

                        -webkit-backdrop-filter: blur(25px);


 

                        border: 1px solid rgba(0, 0, 0, 0.05);


 

                        border-radius: 24px;


 

                        box-shadow: 0 10px 30px rgba(0,0,0,0.02); 

                    }


 

                    .custom-table thead th {


 

                        background: transparent;


 

                        color: #adb5bd;


 

                        text-transform: uppercase;


 

                        font-size: 0.7rem;


 

                        letter-spacing: 1px;


 

                        border-bottom: 2px solid #f1f3f5;


 

                    }


 

                    .animate-in { animation: slideUp 0.5s ease-out forwards; }


 

                    @keyframes slideUp { from { opacity: 0; transform: translateY(15px); } to { opacity: 1; transform: translateY(0); } }


 

                    .btn-vivid {


 

                        background: linear-gradient(90deg, #ff7e5f 0%, #ff6b6b 100%);


 

                        color: white; border: none; border-radius: 12px; font-weight: 700; transition: transform 0.2s;


 

                    }


 

                    .btn-vivid:hover { transform: translateY(-2px); color: white; box-shadow: 0 5px 15px rgba(255, 126, 95, 0.3); }


 

                    .form-control-custom { background: #fff !important; border: 1px solid #dee2e6 !important; border-radius: 10px; padding: 10px 15px; }


 

                    `}


 

                </style>





 

                {/* --- 1. SCHEDULING --- */}


 

                {activeTab === 'scheduling' && (


 

                    <div className="animate-in">


 

                        <div className="d-flex justify-content-between align-items-center mb-4 px-2">


 

                            <h2 className="fw-bold m-0" style={{ color: '#111' }}>Doctor Scheduling</h2>


 

                            <button className="btn btn-vivid px-4 shadow-sm" onClick={() => openForm(null)}>+ Add New Doctor</button>


 

                        </div>





 

                        <div className="glass-panel p-4">


 

                            <div className="table-responsive">


 

                                <table className="table custom-table align-middle mb-0">


 

                                    <thead>


 

                                        <tr>


 

                                            <th>Doctor Name</th><th>Department</th><th>Working Days</th><th>Hours</th><th>Slot</th><th className="text-end">Actions</th>


 

                                        </tr>


 

                                    </thead>


 

                                    <tbody>


 

                                        {doctors.length > 0 ? doctors.map(doc => (


 

                                            <tr key={doc.id}>


 

                                                <td className="fw-bold" style={{ color: '#2c3e50' }}>{doc.name}</td>


 

                                                <td><span className="badge rounded-pill px-3 py-2" style={{ background: 'rgba(255,126,95,0.1)', color: '#ff7e5f' }}>{doc.specialization}</span></td>


 

                                                <td className="small text-muted fw-bold">{doc.days.join(", ")}</td>


 

                                                <td className="small fw-bold">{doc.timeStart} — {doc.timeEnd}</td>


 

                                                <td><b style={{ color: '#ff7e5f' }}>{doc.slot}</b></td>


 

                                                <td className="text-end">


 

                                                    <div className="d-flex gap-2 justify-content-end">


 

                                                        <button className="btn btn-sm fw-bold px-3 border" style={{ borderRadius: '8px', color: '#ffc107' }} onClick={() => openForm(doc)}>Modify</button>


 

                                                        <button className="btn btn-sm fw-bold px-3 border" style={{ borderRadius: '8px', color: '#ff6b6b' }} onClick={() => handleDeleteDoctor(doc.id)}>Delete</button>


 

                                                    </div>


 

                                                </td>


 

                                            </tr>


 

                                        )) : <tr><td colSpan={6} className="text-center py-5 text-muted italic">No doctors registered yet.</td></tr>}


 

                                    </tbody>


 

                                </table>


 

                            </div>


 

                        </div>


 

                    </div>


 

                )}





 

                {/* --- 2. CLINIC RULES --- */}


 

                {activeTab === 'rules' && (


 

                    <div className="animate-in">


 

                        <h2 className="fw-bold mb-4 px-2">System Rules</h2>


 

                        <div className="glass-panel p-5 col-lg-7">


 

                            <div className="mb-4">


 

                                <label className="small fw-bold text-muted text-uppercase mb-2">Number of Persons Allowed Per Slot</label>


 

                                <input type="number" className="form-control form-control-custom" value={maxPatients} onChange={(e) => setMaxPatients(parseInt(e.target.value) || 0)} min={1} />


 

                            </div>


 

                            <div className="mb-4">


 

                                <label className="small fw-bold text-muted text-uppercase mb-2">Max Advance Booking (Days)</label>


 

                                <input type="number" className="form-control form-control-custom" value={maxDays} onChange={(e) => setMaxDays(parseInt(e.target.value))} />


 

                            </div>


 

                            <div className="mb-5">


 

                                <label className="small fw-bold text-muted text-uppercase mb-2">Reschedule & Cancellation Window (Hours)</label>


 

                                <input type="number" className="form-control form-control-custom" value={cutoffHours} onChange={(e) => setCutoffHours(parseInt(e.target.value))} />


 

                            </div>


 

                            <button className="btn btn-vivid w-100 py-3 fw-bold shadow" onClick={handleUpdateRules}>SAVE SYSTEM RULES</button>


 

                        </div>


 

                    </div>


 

                )}





 

                {/* --- 3. ANALYTICS --- */}


 

                {activeTab === 'analytics' && (


 

                    <div className="animate-in">


 

                        <h2 className="fw-bold mb-4 px-2">Clinic Analytics</h2>


 

                        <div className="row g-4 mb-5">


 

                            {[


 

                                { label: 'Total Booked', val: 45, col: '#007bff' },


 

                                { label: 'Walk-ins', val: 12, col: '#20c997' },


 

                                { label: 'Completed', val: 50, col: '#ff7e5f' },


 

                                { label: 'No-Shows', val: 7, col: '#ff6b6b' }


 

                            ].map((s, i) => (


 

                                <div key={i} className="col-6 col-md-3">


 

                                    <div className="glass-panel p-4 text-center h-100 border-top border-4 shadow-sm" style={{ borderTopColor: s.col }}>


 

                                        <h6 className="text-muted small fw-bold text-uppercase mb-2">{s.label}</h6>


 

                                        <h1 className="fw-bold m-0" style={{ color: s.col }}>{s.val}</h1>


 

                                    </div>


 

                                </div>


 

                            ))}


 

                        </div>


 

                    </div>


 

                )}





 

                {/* --- MODAL FORM --- */}


 

                {showForm && (


 

                    <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center" style={{ backgroundColor: 'rgba(255,255,255,0.85)', zIndex: 1050, backdropFilter: 'blur(10px)' }}>


 

                        <div className="glass-panel p-5 animate-in" style={{ width: '520px', maxHeight: '90vh', overflowY: 'auto', background: '#fff' }}>


 

                            <h3 className="fw-bold mb-4" style={{ color: '#111' }}>{editingDoctor ? `Configure Schedule` : 'Add New Professional'}</h3>


 

                            <form onSubmit={handleSaveDoctor}>


 

                                {!editingDoctor && (


 

                                    <div className="row g-3 mb-1">


 

                                        <div className="col-6">

                                            <div className='input-group'>

                                                <span className='input-group-text bg-light border-end-0 text-muted fw-bold'

                                                    style={{ borderRadius: '10px 0 0 10px', fontSize: '0.9rem' }}>

                                                    Dr.

                                                </span>

                                                <input name="name" placeholder="Full Name"

                                                    value={formInputs.name}

                                                    onChange={handleFieldChange}

                                                    className="form-control form-control-custom shadow-none"

                                                    style={{ borderRadius: '0 10px 10px 0', borderLeft: 'none' }} required />

                                                <div style={{ minHeight: '10px' }}>

                                                    {formInputs.name && !/^[a-zA-Z\s]{2,50}$/.test(formInputs.name) && (

                                                        <small className='text-danger' style={{ fontSize: '9px', display: 'block' }}>

                                                            * Name should be at least 2 letters.

                                                        </small>


 

                                                    )}


 

                                                </div>

                                            </div>

                                        </div>



 

                                        <div className="col-6">

                                            <input name="email" type="email"

                                                value={formInputs.email}

                                                placeholder="Email" className="form-control form-control-custom shadow-none" onChange={handleFieldChange} required />

                                            <div style={{ minHeight: '10px' }}>

                                                {formInputs.email && !emailRegex.test(formInputs.email) && (

                                                    <small className='text-danger' style={{ fontSize: '9px' }}>

                                                        * Enter a valid email

                                                    </small>

                                                )}


 

                                            </div>

                                        </div>


 

                                        <div className="col-6">

                                            <input name="password" type="password"

                                                placeholder="Passkey"

                                                value={formInputs.password}

                                                onChange={handleFieldChange} className={`form-control form-control-custom shadow-none ${formInputs.password && !passRegex.test(formInputs.password)?'border-danger':''}`} required />

                                            <div style={{ minHeight: '10px' }}>

                                                {formInputs.password && !passRegex.test(formInputs.password) && (

                                                    <small className='text-danger' style={{ fontSize: '9px', lineHeight: '1' }}>

                                                        * Use 8+ characters with letters,numbers and special characters.

                                                    </small>

                                                )}


 

                                            </div>

                                        </div>


 

                                        <div className="col-6">

                                            <input name="phone" placeholder="Contact no"

                                                value={formInputs.phone}

                                                onChange={handleFieldChange} maxLength={10} className="form-control form-control-custom shadow-none" required />

                                            <div style={{ minHeight: '10px' }}>

                                                {formInputs.phone && !phoneRegex.test(formInputs.phone) && (

                                                    <small className='text-danger' style={{ fontSize: '9px' }}>

                                                        * Enter 10 digits

                                                    </small>

                                                )}


 

                                            </div>

                                        </div>


 

                                        <div className="col-6">

                                            <select name="department" className="form-select form-control-custom" required>

                                                <option value="" disabled selected hidden>Select Department</option>

                                                <option value="GENERAL">GENERAL</option>

                                                <option value="CARDIOLOGY">CARDIOLOGY</option>

                                                <option value="ORTHOPEDICS">ORTHOPEDICS</option>

                                                <option value="PEDIATRICS">PEDIATRICS</option>

                                            </select>

                                        </div>


 

                                        <div className="col-6"><select name="gender" className="form-select form-control-custom" required><option value="" disabled selected hidden>Gender</option><option value="MALE">MALE</option><option value="FEMALE">FEMALE</option></select></div>


 

                                        <div className="col-12">

                                            <input name="location" placeholder="Consultation Room No." className="form-control form-control-custom shadow-none"

                                            value={formInputs.location}

                                            onChange={handleFieldChange}

                                            required />

                                            <div style={{minHeight:'10px'}}>

                                                {formInputs.location && formInputs.location.trim().length<2 && (

                                                    <small className='text-danger' style={{fontSize:'9px'}}>

                                                        * Please enter a room no.

                                                    </small>

                                                )}

                                            </div>

                                        </div>


 

                                        <div className="col-12">

                                            <textarea name="description" placeholder="Description" className="form-control form-control-custom shadow-none" rows={2}

                                            value={formInputs.description}

                                            onChange={handleFieldChange}

                                            required></textarea>

                                            <div style={{minHeight:'10px'}}>

                                                {formInputs.description && formInputs.description.trim().length<10 && (

                                                    <small className='text-danger' style={{ fontSize:'9px'}}>* Bio should be atleast 10 characters long.</small>

                                                )}

                                            </div>

                                        </div>


 

                                    </div>


 

                                )}


 

                                <div className="mb-4">


 

                                    <label className="fw-bold small text-muted mb-2">Operating Days</label>


 

                                    <div className="d-flex flex-wrap gap-2">


 

                                        {allDays.map(day => (


 

                                            <div key={day}>


 

                                                <input type="checkbox" className="btn-check" id={`btn-${day}`} checked={selectedDays.includes(day)} onChange={(e) => e.target.checked ? setSelectedDays([...selectedDays, day]) : setSelectedDays(selectedDays.filter(d => d !== day))} />


 

                                                <label className={`btn btn-sm px-3 rounded-pill fw-bold ${selectedDays.includes(day) ? 'btn-danger text-white border-0' : 'btn-outline-secondary'}`} htmlFor={`btn-${day}`}>{day}</label>


 

                                            </div>


 

                                        ))}


 

                                    </div>

                                    {selectedDays.length === 0 && (

                                        <small className='text-danger d-block mt-1' style={{ fontSize: '10px' }}>

                                            * Select atleast one working day.

                                        </small>

                                    )}


 

                                </div>


 

                                <div className="row g-3 mb-4">


 

                                    <div className="col-6"><label className="small fw-bold text-muted mb-1">Shift Start</label><input type="time" name="timeStart" className="form-control form-control-custom" defaultValue={editingDoctor?.timeStart || "09:00"} /></div>


 

                                    <div className="col-6"><label className="small fw-bold text-muted mb-1">Shift End</label><input type="time" name="timeEnd" className="form-control form-control-custom" defaultValue={editingDoctor?.timeEnd || "17:00"} /></div>


 

                                    <div className="col-12">

                                        <label className="small fw-bold text-muted mb-2">Slot duration</label>

                                        <select name="slot" className="form-select form-control-custom" defaultValue={editingDoctor?.slot || "15 mins"}>

                                            <option value="" disabled selected hidden>slot time</option>

                                            <option value="10 mins">10 mins</option>

                                            <option value="15 mins">15 mins</option>

                                            <option value="20 mins">20 mins</option>

                                        </select>

                                    </div>


 

                                </div>


 

                                <div className="d-flex gap-3">


 

                                    <button type="submit"

                                        disabled={!isFormValid}

                                        className="btn btn-vivid flex-grow-1 py-3 fw-bold"

                                        style={{

                                            background: !isFormValid ? '#adb5bd' : 'linear-gradient(90deg, #ff7e5f 0%, #ff6b6b 100%)',

                                            cursor: !isFormValid ? 'not-allowed' : 'pointer',

                                            opacity: !isFormValid ? 0.7 : 1

                                        }}

                                    >DEPLOY UPDATES</button>


 

                                    <button type="button" className="btn btn-outline-dark px-4 rounded-3 border-0 fw-bold" onClick={closeForm}>Cancel</button>


 

                                </div>


 

                            </form>


 

                        </div>


 

                    </div>


 

                )}


 

            </div>


 

        </div>


 

    );


 

};





 

export default AdminDashboard;