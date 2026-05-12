// import React, { useState, useEffect } from 'react';

// import axios from 'axios';

// import axiosInstance from '../../../services/axiosInstance';

// interface HistoryProps {

//   onNavigate: (tab: 'book' | 'my' | 'history') => void;

// }


 

// const PastHistory: React.FC<HistoryProps> = ({ onNavigate }) => {

//   const [searchTerm, setSearchTerm] = useState("");

//   const [filterMode, setFilterMode] = useState<'dr' | 'date'>('dr');

//   const [selectedVisit, setSelectedVisit] = useState<any>(null);



 

//   const [historyData, setHistoryData] = useState<any[]>([]);

//   const [loading, setLoading] = useState(false);

//   const [startDate, setStartDate] = useState("");

//   const [endDate, setEndDate] = useState("");



//   const fetchHistory = async () => {

//     if (filterMode === 'date' && !startDate && !endDate) return;

//     setLoading(true);

//     try {

//       const response = await axiosInstance.get(`/clinicq/patient/appointments/history`, {

//         params: {

//           startDate: filterMode === 'date' ? startDate : null,

//           endDate: filterMode === 'date' ? endDate : null,

         

//         }

//       });

//       setHistoryData(response.data);

//     } catch (err) {

//       console.error("Error fetching history: ", err);

//     } finally {

//       setLoading(false);

//     }

//   };

//   useEffect(() => {

//     fetchHistory();

//   }, [filterMode, startDate, endDate]);


 

//   const filteredHistory = historyData.filter(item => {

//     if (!searchTerm && filterMode === 'dr') return true;


 

//     if (filterMode === 'dr') {

//       //return item.dr.toLowerCase().includes(searchTerm.toLowerCase());

//       const drName = item.doctor?.name || "";

//       return drName.toLowerCase().includes(searchTerm.toLowerCase());

//     } else {


 

//       const dateValue = item.slotDate || item.timeSlot?.slotDate || item.slot?.slotDate || item.date;


 

//       if (!startDate || !endDate || !dateValue) return false;

//       const recordDate = new Date(dateValue);

//       const start = new Date(startDate);

//       const end = new Date(endDate);

//       if (isNaN(recordDate.getTime()) || isNaN(start.getTime()) || isNaN(end.getTime())) {

//         console.error("invalid date detected", { dateValue, startDate, endDate });

//         return false;

//       }

//       recordDate.setHours(0, 0, 0, 0);

//       start.setHours(0, 0, 0, 0);

//       end.setHours(0, 0, 0, 0);

//       return recordDate >= start && recordDate <= end;


 

//     }

//   });


 

//   return (

//     <div className="d-flex bg-black text-white vh-100 overflow-hidden">


 

//       {/* SIDEBAR */}

//       <div className="bg-dark border-end border-secondary p-2 d-flex flex-column" style={{ width: '210px' }}>

//         <div className="d-flex align-items-center mb-3 mt-2 ps-2 text-info">

//           <div className="bg-info rounded-circle me-2" style={{ width: '22px', height: '22px' }}></div>

//           <h5 className="fw-bold mb-0">ClinicQ</h5>

//         </div>

//         <div className="nav flex-column gap-2">

//           <button onClick={() => onNavigate('book')} className="btn btn-outline-secondary text-white text-start py-2 px-3 rounded-3 border-0 btn-sm">📅 Book Appt</button>

//           <button onClick={() => onNavigate('my')} className="btn btn-outline-secondary text-white text-start py-2 px-3 rounded-3 border-0 btn-sm">📋 My Appts</button>

//           <button onClick={() => onNavigate('history')} className="btn btn-info text-start py-2 px-3 rounded-3 fw-bold btn-sm shadow">🕒 History</button>

//           <div className="border-top border-secondary mt-auto pt-2">

//             <button onClick={() => window.location.reload()} className="btn btn-outline-danger w-100 text-start py-2 px-3 rounded-3 border-0 btn-sm d-flex align-items-center"><span className="me-2">📤</span> Log Out</button>

//           </div>

//         </div>

//       </div>


 

//       {/* MAIN CONTENT */}

//       <div className="flex-grow-1 p-3 d-flex flex-column overflow-hidden">


 

//         {/* FILTER BAR */}

//         <div className="d-flex justify-content-between align-items-center mb-3 bg-dark p-2 rounded-4 border border-secondary border-opacity-25 shadow-sm">

//           <div className="ps-2">

//             <h4 className="fw-bold mb-0">Medical History</h4>

//           </div>


 

//           <div className="d-flex align-items-center gap-3">

//             {/* Filter Mode Toggle */}

//             <div className="btn-group btn-group-sm rounded-pill overflow-hidden border border-secondary">

//               <button

//                 className={`btn px-3 ${filterMode === 'dr' ? 'btn-info text-black fw-bold' : 'btn-dark text-secondary'}`}

//                 onClick={() => {

//                   setFilterMode('dr');

//                   //setSearchTerm(""); // CRITICAL: This clears the date so the full list returns

//                 }}

//               >👨‍⚕️ By Doctor</button>


 

//               <button

//                 className={`btn px-3 ${filterMode === 'date' ? 'btn-info text-black fw-bold' : 'btn-dark text-secondary'}`}

//                 onClick={() => {

//                   setFilterMode('date');

//                   //setSearchTerm(""); // CRITICAL: This clears any text search so the calendar starts fresh

//                 }}

//               >📅 By Date</button>

//             </div>


 

//             {filterMode === 'dr' ? (

//               <input type='text' className='form-control form-control-sm bg-black border-secondary text-white rounded-pill px-3 shadow-none'

//                 placeholder="Enter doctor name..." value={searchTerm}

//                 onChange={(e) => setSearchTerm(e.target.value)}

//                 style={{ width: '200px' }} />

//             ) : (

//               <div className="d-flex gap-2 align-items-center">

//                 <input type='date' className='form-control form-control-sm bg-black border-secondary text-white rounded-pill px-2 shadow-none'

//                   value={startDate}

//                   onChange={(e) => setStartDate(e.target.value)}

//                   style={{ colorScheme: 'dark', fontSize: '12px' }} />

//                 <span className='text-secondary small'>to</span>

//                 <input type='date'

//                   className='form-control form-control-sm bg-black border-secondary text-white rounded-pill px-2 shadow-none'

//                   value={endDate}

//                   onChange={(e) => setEndDate(e.target.value)}

//                   style={{ colorScheme: 'dark', fontSize: '12px' }} />

//               </div>


 

//             )}

//           </div>

//         </div>


 

//         <div className="row g-3 h-100 overflow-hidden">

//           {/* TIMELINE LIST - Dynamically adjusts width */}

//           <div className={`${selectedVisit ? 'col-md-7' : 'col-md-12'} h-100 overflow-auto transition-all`} style={{ transition: '0.3s ease' }}>

//             {loading ? (

//               <div className='text-center mt-5'>

//                 <div className='spinner-border text-info'></div></div>) : (

//               <div className="position-relative ms-3 border-start border-secondary border-opacity-25 ps-4 py-2">

//                 {filteredHistory.map((visit) => (

//                   <div key={visit.id} className='position-relative mb-3'>

//                     <div className='position-absolute bg-info rounded-circle' style={{ width: '10px', height: '10px', left: '-30px', top: '15px' }}></div>

//                     <div className={`card bg-dark border-0 rounded-4 p-3 ${selectedVisit?.id === visit.id ? 'border border-info' : ''}`}

//                       style={{ backgroundColor: '#111', cursor: 'pointer' }}

//                       onClick={() => setSelectedVisit(visit)}>

//                       <div className="d-flex justify-content-between align-items-center">

//                         <div>

//                           <small className="text-info fw-bold d-block mb-1"

//                             style={{ fontSize: '9px' }}>{visit.timeSlot?.slotDate.toUpperCase()}</small>

//                           <h6 className="mb-0 fw-bold text-white">{visit.doctor?.name}</h6>

//                           <div className="text-secondary small opacity-75">{visit.reason}</div>

//                         </div>

//                         <button className="btn btn-outline-info btn-xs rounded-pill px-3 fw-bold" style={{ fontSize: '9px' }}>VIEW VISIT</button>

//                       </div>

//                     </div>

//                   </div>


 

//                 ))}

//               </div>

//             )}

//           </div>

         


 

//           {/* VISIT DETAILS - Slides in from right */}

//           {selectedVisit && (

//             <div className="col-md-5 h-100 animate-slide-in">

//               <div className="card bg-dark border-0 rounded-4 p-4 h-100 shadow-lg d-flex flex-column" style={{ backgroundColor: '#161b22' }}>

//                 <div className="d-flex justify-content-between align-items-center mb-3">

//                   <h5 className="text-info fw-bold mb-0">Visit Record</h5>

//                   <button className="btn btn-sm btn-outline-secondary rounded-circle border-0" onClick={() => setSelectedVisit(null)}>✕</button>

//                 </div>


 

//                 <div className="mb-3">

//                   <small className="text-secondary fw-bold d-block mb-1" style={{ fontSize: '9px' }}>SUMMARY</small>

//                   <p className="fw-bold text-white mb-1" style={{ fontSize: '15px' }}>{selectedVisit.reason}</p>

//                   <span className="badge bg-info bg-opacity-10 text-info border border-info rounded-pill px-2 py-1" style={{ fontSize: '9px' }}>{selectedVisit.type}</span>

//                 </div>


 

//                 <div className="mb-3">

//                   <small className="text-secondary fw-bold d-block mb-1" style={{ fontSize: '9px' }}>VITALS</small>

//                   <div className="p-2 rounded-3 bg-black border border-secondary text-info fw-bold" style={{ fontSize: '12px' }}>

//                     BP: 120/80, HR: 72bpm

//                   </div>

//                 </div>


 

//                 <div className="mb-2">

//                   <small className="text-secondary fw-bold d-block mb-1" style={{ fontSize: '9px' }}>PRESCRIPTION DETAILS</small>

//                   <div className="p-3 rounded-3 bg-black border border-info border-dashed shadow-sm">

//                     <div className="d-flex align-items-center text-white mb-1">

//                       <span className="me-2 fs-3">💊</span>

//                       <span className="fw-bold" style={{ fontSize: '14px' }}>Amlodipine 5mg</span>

//                     </div>

//                     <small className="text-secondary opacity-50" style={{ fontSize: '10px' }}>Take once daily as prescribed.</small>

//                   </div>

//                 </div>


 

//                 <button className="btn btn-outline-secondary w-100 rounded-pill mt-auto py-2 mb-2 border-secondary border-opacity-50 transition-all shadow-sm"

//                   onClick={() => setSelectedVisit(null)}

//                   style={{

//                     fontSize: '12px',

//                     fontWeight: '700',

//                     letterSpacing: '1px',

//                     backgroundColor: '#ffffff05'

//                   }}>CLOSE RECORD</button>

//               </div>

//             </div>

//           )}

//         </div>

//       </div>

//     </div>

//   );

// };


 

// export default PastHistory;



 

import React, { useState, useEffect, useCallback } from 'react';


 

import axiosInstance from '../../../services/axiosInstance';



 




 

interface HistoryProps {


 

  onNavigate: (tab: 'book' | 'my' | 'history') => void;


 

}



 




 

const PastHistory: React.FC<HistoryProps> = ({ onNavigate }) => {


 

  const [searchTerm, setSearchTerm] = useState("");


 

  const [filterMode, setFilterMode] = useState<'dr' | 'date'>('dr');


 

  const [selectedVisit, setSelectedVisit] = useState<any>(null);


 

  const [historyData, setHistoryData] = useState<any[]>([]);


 

  const [loading, setLoading] = useState(false);


 

  const [startDate, setStartDate] = useState("");


 

  const [endDate, setEndDate] = useState("");



 




 

  // --- LOGIC (STRICTLY UNTOUCHED) ---


 

  const fetchHistory = useCallback(async () => {


 

    if (filterMode === 'date' && !startDate && !endDate) return;


 

    setLoading(true);


 

    try {


 

      const response = await axiosInstance.get(`/clinicq/patient/appointments/history`, {


 

        params: {


 

          startDate: filterMode === 'date' ? startDate : null,


 

          endDate: filterMode === 'date' ? endDate : null,


 

        }


 

      });


 

      setHistoryData(response.data);


 

    } catch (err) {


 

      console.error("Error fetching history: ", err);


 

    } finally {


 

      setLoading(false);


 

    }


 

  }, [filterMode, startDate, endDate]);



 




 

  useEffect(() => {


 

    fetchHistory();


 

  }, [fetchHistory]);



 




 

  const filteredHistory = historyData.filter(item => {


 

    if (!searchTerm && filterMode === 'dr') return true;


 

    if (filterMode === 'dr') {


 

      const drName = item.doctor?.name || "";


 

      return drName.toLowerCase().includes(searchTerm.toLowerCase());


 

    } else {


 

      const dateValue = item.slotDate || item.timeSlot?.slotDate || item.slot?.slotDate || item.date;


 

      if (!startDate || !endDate || !dateValue) return false;


 

      const recordDate = new Date(dateValue);


 

      const start = new Date(startDate);


 

      const end = new Date(endDate);


 

      if (isNaN(recordDate.getTime()) || isNaN(start.getTime()) || isNaN(end.getTime())) return false;


 

      recordDate.setHours(0, 0, 0, 0);


 

      start.setHours(0, 0, 0, 0);


 

      end.setHours(0, 0, 0, 0);


 

      return recordDate >= start && recordDate <= end;


 

    }


 

  });



 




 

  return (


 

    <div className="d-flex min-vh-100 overflow-hidden"


 

         style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>



 




 

      <style>


 

        {`


 

          .glass-panel-light { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(25px); -webkit-backdrop-filter: blur(25px); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 20px; }


 

          .history-card { background: #ffffff; border: 1px solid #eee; border-radius: 16px; transition: all 0.3s ease; }


 

          .history-card:hover { transform: translateX(5px); box-shadow: 0 5px 15px rgba(0,0,0,0.05); border-color: #20c997; }


 

          .active-record { border-left: 5px solid #20c997 !important; background: rgba(32, 201, 151, 0.05); }


 

          .timeline-dot { width: 12px; height: 12px; background: #20c997; border: 3px solid #fff; box-shadow: 0 0 0 3px rgba(32, 201, 151, 0.1); }


 

          .custom-input { background: #fff !important; border: 1px solid #dee2e6 !important; font-weight: 600; }


 

          .animate-slide-in { animation: slideIn 0.4s ease-out; }


 

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


 

          @keyframes slideIn { from { opacity: 0; transform: translateX(20px); } to { opacity: 1; transform: translateX(0); } }




 

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


 

          <button onClick={() => onNavigate('book')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">📅 Book Appointment</button>


 

          <button onClick={() => onNavigate('my')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">📋 My Appointments</button>


 

          <button onClick={() => onNavigate('history')} className="btn text-start py-2 px-3 border-0 fw-bold" style={{ background: 'rgba(32, 201, 151, 0.1)', color: '#198754', borderRadius: '12px' }}>🕒 History</button>


 

          <div className="mt-auto pt-4 border-top">

            <button onClick={() => window.location.reload()} className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-2"

              style={{fontSize:'13px'}}><span>📤</span> Log Out</button>

          </div>


 

        </div>


 

      </div>



 




 

      {/* MAIN CONTENT */}


 

      <div className="flex-grow-1 p-4 d-flex flex-column overflow-hidden">


 

       


 

        {/* FILTER HEADER */}


 

        <div className="glass-panel-light p-3 mb-4 d-flex justify-content-between align-items-center shadow-sm">


 

          <h3 className="fw-bold m-0 ps-2" style={{ color: '#111' }}>Medical Vault</h3>


 

         


 

          <div className="d-flex align-items-center gap-3">


 

            <div className="btn-group p-1 rounded-pill" style={{ background: '#f1f3f5' }}>


 

              <button className={`btn btn-sm rounded-pill px-3 fw-bold border-0 ${filterMode === 'dr' ? 'bg-white shadow-sm text-dark' : 'text-muted'}`} onClick={() => setFilterMode('dr')}>👨‍⚕️ Professional</button>


 

              <button className={`btn btn-sm rounded-pill px-3 fw-bold border-0 ${filterMode === 'date' ? 'bg-white shadow-sm text-dark' : 'text-muted'}`} onClick={() => setFilterMode('date')}>📅 Date Range</button>


 

            </div>



 




 

            {filterMode === 'dr' ? (


 

              <input type="text" className="form-control form-control-sm custom-input rounded-pill px-3" placeholder="Search Doctor..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} style={{ width: '220px' }} />


 

            ) : (


 

              <div className="d-flex gap-2 align-items-center">


 

                <input type="date" className="form-control form-control-sm custom-input rounded-pill" value={startDate} onChange={(e) => setStartDate(e.target.value)} />


 

                <span className="text-muted small fw-bold">To</span>


 

                <input type="date" className="form-control form-control-sm custom-input rounded-pill" value={endDate} onChange={(e) => setEndDate(e.target.value)} />


 

              </div>


 

            )}


 

          </div>


 

        </div>



 




 

        <div className="row g-4 h-100 overflow-hidden">


 

          {/* TIMELINE LIST */}


 

          <div className={`${selectedVisit ? 'col-md-7' : 'col-md-12'} h-100 overflow-auto custom-scrollbar transition-all`}>


 

            {loading ? (


 

              <div className="text-center mt-5"><div className="spinner-border" style={{ color: '#20c997' }}></div></div>


 

            ) : (


 

              <div className="ms-4 border-start ps-4 py-2" style={{ borderColor: '#dee2e6' }}>


 

                {filteredHistory.map((visit) => (


 

                  <div key={visit.id} className="position-relative mb-4">


 

                    <div className="position-absolute timeline-dot rounded-circle" style={{ left: '-31px', top: '20px' }}></div>


 

                    <div className={`history-card p-3 shadow-sm ${selectedVisit?.id === visit.id ? 'active-record' : ''}`} onClick={() => setSelectedVisit(visit)} style={{ cursor: 'pointer' }}>


 

                      <div className="d-flex justify-content-between align-items-center">


 

                        <div>


 

                          <small className="fw-bold text-uppercase d-block mb-1" style={{ color: '#20c997', fontSize: '10px', letterSpacing: '1px' }}>{visit.timeSlot?.slotDate}</small>


 

                          <h5 className="fw-bold mb-1" style={{ color: '#2c3e50' }}>{visit.doctor?.name}</h5>


 

                          <p className="text-muted small mb-0">{visit.reason}</p>


 

                        </div>


 

                        <button className="btn btn-sm rounded-pill px-3 fw-bold" style={{ background: '#f8f9fa', color: '#20c997', fontSize: '10px', border: '1px solid #eee' }}>VIEW DOSSIER</button>


 

                      </div>


 

                    </div>


 

                  </div>


 

                ))}


 

              </div>


 

            )}


 

          </div>



 




 

          {/* VISIT DETAILS PANEL */}


 

          {selectedVisit && (


 

            <div className="col-md-5 h-100 animate-slide-in">


 

              <div className="glass-panel-light p-4 h-100 d-flex flex-column shadow-lg border-start" style={{ background: '#fff' }}>


 

                <div className="d-flex justify-content-between align-items-center mb-3 border-bottom pb-3">


 

                  <h4 className="fw-bold m-0" style={{ color: '#111' }}>Clinical Record</h4>


 

                  <button className="btn btn-sm btn-light rounded-circle shadow-none" onClick={() => setSelectedVisit(null)}>✕</button>


 

                </div>


 

               


 

                <div className="mb-3">


 

                  <label className="text-muted fw-bold small text-uppercase d-block mb-2">Diagnosis / Reason</label>


 

                  <div className="p-3 rounded-4" style={{ background: '#f8f9fa', borderLeft: '4px solid #20c997' }}>


 

                    <p className="fw-bold mb-1" style={{ fontSize: '1rem' }}>{selectedVisit.reason}</p>


 

                    <span className="badge rounded-pill bg-success-subtle text-success px-3">{selectedVisit.status || 'Completed'}</span>


 

                  </div>


 

                </div>



 




 

                <div className="mb-3">


 

                  <label className="text-muted fw-bold small text-uppercase d-block mb-2">Vitals Captured</label>


 

                  <div className="row g-2">


 

                    <div className="col-6"><div className="p-2 rounded-3 text-center border" style={{ background: '#fff' }}><small className="d-block text-muted">Blood Pressure</small><b style={{ color: '#20c997' }}>120/80</b></div></div>


 

                    <div className="col-6"><div className="p-2 rounded-3 text-center border" style={{ background: '#fff' }}><small className="d-block text-muted">Heart Rate</small><b style={{ color: '#20c997' }}>72 BPM</b></div></div>


 

                  </div>


 

                </div>



 




 

                <div className="mb-3 flex-grow-1">


 

                  <label className="text-muted fw-bold small text-uppercase d-block mb-2">Prescription</label>


 

                  <div className="p-3 rounded-4 border-dashed border-2 d-flex align-items-center" style={{ background: 'rgba(32, 201, 151, 0.03)', borderColor: '#20c997', borderStyle: 'dashed' }}>


 

                    <span className="fs-2 me-3">💊</span>


 

                    <div>


 

                      <h6 className="fw-bold mb-0">Medical Prescription Available</h6>


 

                      <small className="text-muted">Digital Rx Certified</small>


 

                    </div>


 

                  </div>


 

                </div>

                <button className="btn btn-dark w-100 mb-2 py-2 rounded-pill fw-bold shadow-sm mt-auto" onClick={() => setSelectedVisit(null)}>CLOSE ARCHIVE</button>

              </div>


 

            </div>


 

          )}


 

        </div>


 

      </div>


 

    </div>


 

  );


 

};



 




 

export default PastHistory;