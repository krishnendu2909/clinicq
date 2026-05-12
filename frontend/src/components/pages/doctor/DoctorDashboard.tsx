// import React, { useState, useEffect, useCallback } from 'react';

// import axios from 'axios';

// import axiosInstance from '../../../services/axiosInstance';

// interface Patient {

//   id: number; // Add this

//   appointmentId: number; // For your backend calls

//   token: string;

//   name: string;

//   time: string;

//   status: 'COMPLETED' | 'IN_CONSULTATION' | 'WAITING' | 'BOOKED' | 'NO_SHOW';

// }


 

// const DoctorDashboard: React.FC = () => {

//   const [patients, setPatients] = useState<Patient[]>([]);

//   const [loading, setLoading] = useState(true);

//   // 2. Add the Fetch Function

//   const fetchSchedule = useCallback(async () => {

//     setLoading(true);

//     try {

//       //const doctorId = 4; // Use logged-in doctor ID

//       const response = await axiosInstance.get(`/clinicq/doctor/schedule`);


 

//       const mappedData = response.data.map((t: any) => ({

//         id: t.id,

//         appointmentId: t.appointment?.id,

//         token: `TKN-${t.tokenNumber}`,

//         name: t.patient?.name || "Unknown Patient", // Navigation: t -> patient -> name

//         // Navigation: t -> appointment -> timeSlot -> startTime

//         time: t.appointment?.timeSlot?.startTime?.substring(0, 5) || "00:00",

//         status: t.status // This is 'WAITING' or 'IN_CONSULTATION'

//       }));


 

//       setPatients(mappedData);

//     } catch (err) {

//       console.error("Failed to load schedule", err);

//     } finally {

//       setLoading(false);

//     }

//   }, []);




 

//   useEffect(() => {

//     fetchSchedule();

//   }, [fetchSchedule]);


 

//   const timeToMinutes = (timeStr: string) => {

//     const [time, modifier] = timeStr.split(' ');

//     let [hours, minutes] = time.split(':').map(Number);

//     if (hours === 12) hours = 0;

//     if (modifier === 'PM') hours += 12;

//     return hours * 60 + minutes;

//   };


 

//   // 3. Update the Status function to call backend

//   const updateStatus = async (tokenString: string, newStatus: string) => {

//     // 1. Find the actual patient object to get their database ID (id, not tokenNumber)

//     const patient = patients.find(p => p.token === tokenString);

//     if (!patient) return;


 

//     try {

//       // 2. Call the PUT endpoint from your Swagger

//       // Format: /clinicq/doctor/token/{tokenId}/status?status=COMPLETED

//       await axiosInstance.put(`/clinicq/doctor/token/${patient.id}/status`, null, {

//         params: { status: newStatus }

//       });


 

//       // 3. Refresh the UI data after successful update

//       fetchSchedule();

//     } catch (err) {

//       console.error("Status update failed:", err);

//       alert("Failed to update status. Please try again.");

//     }

//   };


 

//   // 1. Sort the full list for the left table

//   //const sortedSchedule = [...patients].sort((a, b) => a.time.localeCompare(b.time));

//   // Place this right before your 'return'

//   // 1. Safe mapping for status and time

//   // 1. Fixed Sorting: Use localeCompare to avoid the split(' ') crash

//   const sortedSchedule = [...patients].sort((a, b) => (a.time || "").localeCompare(b.time || ""));


 

//   // 2. Fixed Filtering: Ensure underscores or hyphens match your DB

//   const currentPatient = patients.find(p => p.status === 'IN_CONSULTATION') ||

//     patients.find(p => p.status === 'WAITING');


 

//   const upcomingList = patients.filter(p =>

//     p.status === 'WAITING' && p.appointmentId !== currentPatient?.appointmentId

//   );


 

//   const nextPatient = upcomingList.length > 0 ? upcomingList[0] : null;

//   const completedToday = patients.filter(p => p.status === 'COMPLETED');



 

//   return (

//     <div className="vh-100 bg-black text-white p-2 overflow-hidden d-flex flex-column">

//       {/* COMPACT HEADER */}

//       <div className="d-flex justify-content-between align-items-center mb-2 px-2">

//         <h4 className="fw-bold text-primary mb-0">Physician Console

//           <small className="text-secondary fs-6 ps-2">Dr. Sarah Devis</small>

//         </h4>

//         <div className="badge bg-dark border border-secondary py-1 px-3 rounded-pill text-info small" style={{ fontSize: '11px' }}>

//           {new Date().toLocaleDateString()}

//         </div>

//       </div>


 

//       {/* MAIN CONTENT ROW */}

//       <div className="row g-2 flex-grow-1 overflow-hidden mx-0" style={{ minHeight: '400px' }}>


 

//         {/* LEFT COLUMN - Today's Schedule & Upcoming */}

//         <div className="col-md-7 d-flex flex-column h-100 overflow-hidden">


 

//           {/* TODAY'S SCHEDULE */}

//           <div className="card bg-dark border-secondary border-opacity-25 rounded-4 p-3 flex-grow-1 d-flex flex-column overflow-hidden mb-2" style={{ backgroundColor: '#0d0d0f' }}>

//             <h6 className="fw-bold text-info mb-3" style={{ fontSize: '12px' }}>Today's Schedule</h6>

//             <div className="table-responsive flex-grow-1 overflow-auto">

//               <table className="table table-dark table-hover align-middle mb-0">

//                 <thead className="sticky-top bg-dark">

//                   <tr className="text-secondary small">

//                     <th>TIME</th><th>PATIENT</th><th className="text-end">STATUS</th>

//                   </tr>

//                 </thead>

//                 <tbody style={{ fontSize: '13px' }}>

//                   {!loading && patients.length > 0 ? (

//                     patients.map((p, index) => (

//                       <tr key={p.appointmentId || index} className="border-secondary border-opacity-10">

//                         <td className="text-info fw-bold">{p.time}</td>

//                         <td>{p.name} <small className="text-muted d-block" style={{ fontSize: '10px' }}>{p.token}</small></td>

//                         <td className="text-end">

//                           <span className={`badge rounded-pill border ${p.status === 'WAITING' ? 'border-info text-info' :

//                               p.status === 'NO_SHOW' ? 'border-danger text-danger' : 'border-primary text-primary'

//                             }`} style={{ fontSize: '10px' }}>

//                             {p.status}

//                           </span>

//                         </td>

//                       </tr>

//                     ))

//                   ) : (

//                     <tr>

//                       <td colSpan={3} className="text-center py-5 text-muted">

//                         {loading ? "Loading Schedule..." : "No appointments found for today."}

//                       </td>

//                     </tr>

//                   )}

//                 </tbody>

//               </table>

//             </div>

//           </div>


 

//           {/* UPCOMING LIST */}

//           <div className="card bg-dark border-0 rounded-4 p-2" style={{ height: '30%', backgroundColor: '#0d0d0f' }}>

//             <h6 className="fw-bold text-secondary mb-2 ps-1" style={{ fontSize: '11px' }}>Upcoming List</h6>

//             <div className="overflow-auto h-100">

//               <table className="table table-dark table-sm mb-0" style={{ fontSize: '11px' }}>

//                 <tbody className="opacity-75">

//                   {upcomingList.map(p => (

//                     <tr key={p.token} className="border-secondary border-opacity-10">

//                       <td className="text-info">{p.token}</td>

//                       <td>{p.name}</td>

//                       <td className="text-end">{p.time}</td>

//                     </tr>

//                   ))}

//                 </tbody>

//               </table>

//             </div>

//           </div>

//         </div>


 

//         {/* RIGHT COLUMN - Consultation & Queue Cards */}

//         <div className="col-md-5 d-flex flex-column gap-2 h-100 overflow-hidden">


 

//           {/* CURRENT QUEUE CARD */}

//           <div className="card border-0 rounded-4 p-3 shadow-lg" style={{ background: 'linear-gradient(145deg, #161625, #0a0a0c)', border: '1px solid #333' }}>

//             <p className="text-info fw-bold mb-1" style={{ fontSize: '9px', letterSpacing: '1px' }}>CURRENT CONSULTATION</p>

//             {currentPatient ? (

//               <>

//                 <h2 className="fw-bold text-white mb-0">{currentPatient.name}</h2>

//                 <p className="text-info small mb-3">{currentPatient.token} | Status: {currentPatient.status}</p>


 

//                 <div className="d-flex flex-column gap-2">

//                   {currentPatient.status !== 'IN_CONSULTATION' ? (

//                     <>

//                       <button className="btn btn-info btn-sm py-2 rounded-pill fw-bold text-black" onClick={() => updateStatus(currentPatient.token, 'IN_CONSULTATION')}>

//                         START CONSULTATION

//                       </button>

//                       <button

//                         className="btn btn-outline-danger btn-sm py-1 rounded-pill fw-bold"

//                         onClick={() => updateStatus(currentPatient.token, 'NO_SHOW')} // Changed from 'No-Show' to 'NO_SHOW'

//                       >

//                         NO-SHOW

//                       </button>

//                     </>

//                   ) : (

//                     <>

//                       <div className="badge bg-primary py-2 mb-1" style={{ fontSize: '10px' }}>IN CONSULTATION</div>

//                       <button className="btn btn-success btn-sm py-2 rounded-pill fw-bold shadow-glow" onClick={() => updateStatus(currentPatient.token, 'COMPLETED')}>

//                         MARK AS COMPLETED

//                       </button>

//                     </>

//                   )}

//                 </div>

//               </>

//             ) : (

//               <div className="py-4 text-center text-secondary small">Queue Empty</div>

//             )}

//           </div>


 

//           {/* NEXT QUEUE CARD */}

//           <div className="card border-0 rounded-4 p-2 bg-dark" style={{ border: '1px solid #222' }}>

//             <p className="text-secondary fw-bold mb-1" style={{ fontSize: '9px' }}>NEXT IN LINE</p>

//             {nextPatient ? (

//               <div className="d-flex justify-content-between align-items-center">

//                 <div>

//                   <h6 className="fw-bold mb-0 text-white" style={{ fontSize: '13px' }}>{nextPatient.name}</h6>

//                   <small className="text-info" style={{ fontSize: '10px' }}>{nextPatient.token}</small>

//                 </div>

//                 <span className="badge bg-secondary opacity-50" style={{ fontSize: '9px' }}>{nextPatient.status}</span>

//               </div>

//             ) : <small className="text-muted small">None</small>}

//           </div>


 

//           {/* COMPLETED TODAY TABLE */}

//           <div className="card border-0 rounded-4 p-2 flex-grow-1 overflow-hidden" style={{ backgroundColor: '#0f0f12' }}>

//             <p className="text-success fw-bold mb-1 ps-1" style={{ fontSize: '10px' }}>COMPLETED TODAY</p>

//             <div className="overflow-auto h-100">

//               <table className="table table-dark table-sm mb-0" style={{ fontSize: '11px' }}>

//                 <tbody className="opacity-50">

//                   {completedToday.map(p => (

//                     <tr key={p.token} className="border-secondary border-opacity-10">

//                       <td className="text-success fw-bold">{p.token}</td>

//                       <td>{p.name}</td>

//                       <td className="text-end">✅</td>

//                     </tr>

//                   ))}

//                 </tbody>

//               </table>

//             </div>

//           </div>


 

//         </div> {/* End Right Column */}

//       </div> {/* End Row */}

//     </div>

//   );

// };


 

// export default DoctorDashboard;




 

import React, { useState, useEffect, useCallback } from 'react';


 

import axiosInstance from '../../../services/axiosInstance';



 




 

interface Patient {


 

  id: number;


 

  appointmentId: number;


 

  token: string;


 

  name: string;


 

  time: string;


 

  status: 'COMPLETED' | 'IN_CONSULTATION' | 'WAITING' | 'BOOKED' | 'NO_SHOW';


 

}



 




 

const DoctorDashboard: React.FC = () => {


 

  const [patients, setPatients] = useState<Patient[]>([]);


 

  const [loading, setLoading] = useState(true);


 

  const [breakMode, setBreakMode] = useState(false);



 




 

  // --- LOGIC (STRICTLY UNTOUCHED) ---


 

  const fetchSchedule = useCallback(async () => {


 

    setLoading(true);


 

    try {


 

      const response = await axiosInstance.get(`/clinicq/doctor/schedule`);


 

      const mappedData = response.data.map((t: any) => ({


 

        id: t.id,


 

        appointmentId: t.appointment?.id,


 

        token: `TKN-${t.tokenNumber}`,


 

        name: t.patient?.name || "Unknown Patient",


 

        time: t.appointment?.timeSlot?.startTime?.substring(0, 5) || "00:00",


 

        status: t.status


 

      }));


 

      setPatients(mappedData);


 

    } catch (err) {


 

      console.error("Failed to load schedule", err);


 

    } finally {


 

      setLoading(false);


 

    }


 

  }, []);



 




 

  useEffect(() => { fetchSchedule(); }, [fetchSchedule]);



 




 

  const updateStatus = async (tokenString: string, newStatus: string) => {


 

    const patient = patients.find(p => p.token === tokenString);


 

    if (!patient) return;


 

    try {


 

      await axiosInstance.put(`/clinicq/doctor/token/${patient.id}/status`, null, {


 

        params: { status: newStatus }


 

      });


 

      fetchSchedule();


 

    } catch (err) {


 

      console.error("Status update failed:", err);


 

    }


 

  };



 




 

  const currentPatient = patients.find(p => p.status === 'IN_CONSULTATION') || patients.find(p => p.status === 'WAITING');


 

  const upcomingList = patients.filter(p => p.status === 'WAITING' && p.appointmentId !== currentPatient?.appointmentId);


 

  const nextPatient = upcomingList.length > 0 ? upcomingList[0] : null;


 

  const completedToday = patients.filter(p => p.status === 'COMPLETED');



 




 

  return (


 

    <div className="d-flex min-vh-100 overflow-hidden"


 

         style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>


 

     


 

      <style>


 

        {`


 

          .glass-panel-dr { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(25px); -webkit-backdrop-filter: blur(25px); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.02); }


 

          .sidebar-dr { width: 260px; background: #ffffff; border-right: 1px solid #eee; display: flex; flex-direction: column; padding: 24px; }


 

          .nav-btn-dr { width: 100%; text-align: left; padding: 12px 16px; border-radius: 12px; border: none; background: transparent; font-weight: 600; color: #6c757d; margin-bottom: 8px; transition: all 0.2s; }


 

          .nav-btn-active { background: rgba(32, 201, 151, 0.1); color: #198754; }


 

          .custom-table-dr thead th { color: #adb5bd; font-size: 0.7rem; text-transform: uppercase; letter-spacing: 1px; border-bottom: 2px solid #f8f9fa; }


 

          .completed-row { border-bottom: 1px solid #f1f3f5; }


 

          .shadow-glow { box-shadow: 0 0 15px rgba(32, 201, 151, 0.3); }


 

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



 




 

      {/* --- SIDEBAR --- */}


 

      <div className="sidebar-dr shadow-sm">


 

        <div className="d-flex align-items-center mb-5 ps-2">


 

          <div className="rounded-circle me-3 d-flex align-items-center justify-content-center fw-bold text-white shadow-sm"


 

               style={{ width: '42px', height: '42px', background: 'linear-gradient(135deg, #0beacb, #20c997)' }}>🩺</div>


 

          <div>


 

            <h5 className="mb-0 fw-bold" style={{ color: '#111' }}>ClinicQ <span style={{color: '#20c997'}}>MD</span></h5>


 

            <small className="text-muted fw-bold" style={{ fontSize: '10px' }}>PHYSICIAN CONSOLE</small>


 

          </div>


 

        </div>



 




 

        <div className="flex-grow-1">


 

          <button className="nav-btn-dr nav-btn-active">📅 Today's Queue</button>


 

          <button className="nav-btn-dr">📝 Prescriptions</button>


 

          <button className="nav-btn-dr">🕒 History</button>


 

          <button className={`nav-btn-dr ${breakMode ? 'bg-danger-subtle text-danger' : ''}`} onClick={() => setBreakMode(!breakMode)}>


 

            {breakMode ? '☕ On Break' : '☕ Break Mode'}


 

          </button>


 

          <button className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-3 px-3 ms-auto"

            style={{

              fontSize: '16px',

              display: 'flex',

              alignItems: 'center',

              gap: '8px'

            }}

            onClick={() => window.location.reload()}>📤 Log Out</button>


 

        </div>



 




 

        {/* <div className="mt-auto pt-4 border-top">


 

          <button className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-2"

          style={{fontSize:'13px'}}

          onClick={() => window.location.reload()}><span>📤</span> Log Out</button>


 

        </div> */}


 

      </div>



 




 

      {/* --- MAIN AREA --- */}


 

      <div className="flex-grow-1 p-4 overflow-hidden">


 

        <div className="row g-4 h-100">


 

           


 

          {/* LEFT COLUMN: LIVE QUEUE */}


 

          <div className="col-lg-7 d-flex flex-column h-100">


 

            <div className="glass-panel-dr p-4 mb-4 d-flex flex-column overflow-hidden" style={{ flex: '2' }}>


 

              <div className="d-flex justify-content-between align-items-center mb-4">


 

                <h5 className="fw-bold m-0" style={{ color: '#20c997' }}>Live Patient Queue</h5>


 

                <span className="badge bg-success-subtle text-success rounded-pill px-3">● Real-time</span>


 

              </div>


 

              <div className="table-responsive flex-grow-1 overflow-auto">


 

                <table className="table custom-table-dr align-middle mb-0">


 

                  <thead><tr><th>Time</th><th>Patient Identity</th><th className="text-end">Status</th></tr></thead>


 

                  <tbody>


 

                    {!loading && patients.map((p, i) => (


 

                      <tr key={p.id || i}>


 

                        <td className="fw-bold text-dark">{p.time}</td>


 

                        <td>


 

                          <div className="fw-bold" style={{ color: '#2c3e50' }}>{p.name}</div>


 

                          <small className="text-muted fw-bold" style={{ fontSize: '9px' }}>{p.token}</small>


 

                        </td>


 

                        <td className="text-end">


 

                          <span className={`badge rounded-pill px-3 py-2 border ${p.status === 'WAITING' ? 'bg-info-subtle text-info border-info' : 'bg-primary text-white border-0'}`} style={{ fontSize: '10px' }}>{p.status}</span>


 

                        </td>


 

                      </tr>


 

                    ))}


 

                  </tbody>


 

                </table>


 

              </div>


 

            </div>



 




 

            <div className="glass-panel-dr p-4" style={{ flex: '1' }}>


 

              <h6 className="fw-bold text-muted small text-uppercase mb-3">Upcoming Appointments</h6>


 

              <div className="row row-cols-1 row-cols-md-2 g-2 overflow-auto">


 

                {upcomingList.map(p => (


 

                  <div className="col" key={p.token}>


 

                    <div className="p-3 rounded-4 border bg-white shadow-sm d-flex justify-content-between align-items-center">


 

                      <div>


 

                        <div className="fw-bold small">{p.name}</div>


 

                        <small className="text-info fw-bold" style={{fontSize: '9px'}}>{p.token}</small>


 

                      </div>


 

                      <div className="text-muted fw-bold small">{p.time}</div>


 

                    </div>


 

                  </div>


 

                ))}


 

              </div>


 

            </div>


 

          </div>



 




 

          {/* RIGHT COLUMN: ACTIVE CALL */}


 

          <div className="col-lg-5 d-flex flex-column gap-3 h-100">


 

            <div className="p-4 rounded-5 shadow-lg" style={{ background: 'linear-gradient(135deg, #2c3e50, #000000)', color: 'white' }}>


 

              <small className="fw-bold opacity-50 text-uppercase ls-1">Active Consultation</small>


 

              {currentPatient ? (


 

                <div className="mt-4 text-center text-md-start">


 

                  <h1 className="fw-bold mb-1 display-6">{currentPatient.name}</h1>


 

                  <div className="badge bg-white bg-opacity-10 text-white rounded-pill border border-white border-opacity-10 mb-4 px-3 py-2">{currentPatient.token}</div>


 

                  <div className="d-flex flex-column gap-2">


 

                    {currentPatient.status !== 'IN_CONSULTATION' ? (


 

                      <>


 

                        <button className="btn btn-light py-3 rounded-pill fw-bold" onClick={() => updateStatus(currentPatient.token, 'IN_CONSULTATION')}>▶ START CONSULTATION</button>


 

                        <button className="btn btn-outline-danger py-2 rounded-pill fw-bold border-0 opacity-75" onClick={() => updateStatus(currentPatient.token, 'NO_SHOW')}>Mark No-Show</button>


 

                      </>


 

                    ) : (


 

                      <button className="btn py-3 rounded-pill fw-bold text-white shadow-glow" style={{ background: '#20c997' }} onClick={() => updateStatus(currentPatient.token, 'COMPLETED')}>✓ MARK AS COMPLETED</button>


 

                    )}


 

                  </div>


 

                </div>


 

              ) : <div className="py-5 text-center opacity-50 italic">Waiting for next patient...</div>}


 

            </div>



 




 

            <div className="glass-panel-dr p-3 d-flex align-items-center justify-content-between border-start border-4 shadow-sm" style={{ borderLeftColor: '#0beacb' }}>


 

              <div>


 

                <small className="text-muted fw-bold text-uppercase" style={{ fontSize: '9px' }}>Next in Line</small>


 

                <h6 className="fw-bold mb-0" style={{ color: '#111' }}>{nextPatient?.name || "No pending queue"}</h6>


 

              </div>


 

              {nextPatient && (


 

                <div className="text-end">


 

                   <div className="badge bg-light text-dark rounded-pill px-3">{nextPatient.time}</div>


 

                   <div className="text-info fw-bold d-block mt-1" style={{fontSize: '9px'}}>{nextPatient.token}</div>


 

                </div>


 

              )}


 

            </div>



 




 

            <div className="glass-panel-dr p-4 flex-grow-1 overflow-hidden d-flex flex-column">


 

              <h6 className="fw-bold text-success small text-uppercase mb-3">Completed Today</h6>


 

              <div className="overflow-auto flex-grow-1">


 

                <table className="table table-sm align-middle mb-0">


 

                  <tbody style={{ fontSize: '12px' }}>


 

                    {completedToday.length > 0 ? (


 

                      completedToday.map(p => (


 

                        <tr key={p.token} className="completed-row">


 

                          <td className="fw-bold text-success py-3" style={{ width: '80px' }}>{p.token}</td>


 

                          <td className="fw-bold">{p.name}</td>


 

                          <td className="text-end text-success">✅</td>


 

                        </tr>


 

                      ))


 

                    ) : (


 

                      <tr><td colSpan={3} className="text-center py-5 text-muted opacity-50 italic">No records yet.</td></tr>


 

                    )}


 

                  </tbody>


 

                </table>


 

              </div>


 

            </div>


 

          </div>



 




 

        </div>


 

      </div>


 

    </div>


 

  );


 

};



 




 

export default DoctorDashboard;