// import React, { useState, useEffect } from 'react';

// import { Patient, Doctor } from './ReceptionistDashboard';

// import axiosInstance from '../../../services/axiosInstance';

// interface NowServingProps {

//   patients: Patient[];

//   doctors: Doctor[];

// }


 

// const NowServing: React.FC<NowServingProps> = ({ patients, doctors }) => {

//   const [currentTime, setCurrentTime] = useState(new Date());


 

//   // Update current time every second

//   useEffect(() => {

//     const timer = setInterval(() => {

//       setCurrentTime(new Date());

//     }, 1000);


 

//     return () => clearInterval(timer);

//   }, []);


 

//   // Get patients by doctor and status

//   const getPatientsByDoctorAndStatus = (doctorId?: string, status?: Patient['status']) => {

//     return patients.filter(patient =>

//       patient.doctor === doctorId &&

//       (!status || patient.status === status)

//     ).sort((a, b) => a.position - b.position);

//   };


 

//   // Get current serving patient (In Consultation) or first waiting patient

//   const getCurrentServing = (doctorId?: string) => {

//     const inConsultation = getPatientsByDoctorAndStatus(doctorId, 'IN_CONSULTATION');

//     if (inConsultation.length > 0) return inConsultation[0];


 

//     const waiting = getPatientsByDoctorAndStatus(doctorId, 'WAITING');

//     return waiting.length > 0 ? waiting[0] : null;

//   };


 

//   // Get next 2-3 patients in queue

//   const getNextPatients = (doctorId?: string, currentPatientId?: string) => {

//     const waiting = getPatientsByDoctorAndStatus(doctorId, 'WAITING');

//     return waiting

//       .filter(p => p.id !== currentPatientId)

//       .slice(0, 3);

//   };


 

//   return (

//     <div className="min-vh-100 bg-gradient bg-dark text-white p-4">

//       {/* Header */}

//       <div className="text-center mb-5">

//         <h1 className="display-3 fw-bold text-info mb-3">Now Serving</h1>

//         <div className="lead text-secondary">

//           {currentTime.toLocaleDateString()} • {currentTime.toLocaleTimeString()}

//         </div>

//       </div>


 

//       {/* Doctor Queues */}

//       <div className="container">

//         <div className="row g-4">

//           {doctors.map((doctor) => {

//             const currentPatient = getCurrentServing(doctor.id);

//             const nextPatients = getNextPatients(doctor.id, currentPatient?.id);


 

//             return (

//               <div key={doctor.id} className="col-lg-6 col-xl-4">

//                 <div className="card bg-secondary border-info h-100">

//                   <div className="card-header bg-info text-dark">

//                     <h3 className="mb-0">{doctor.name}</h3>

//                     <small className="text-muted">{doctor.specialization}</small>

//                   </div>

//                   <div className="card-body">

//                     {/* Current Serving */}

//                     <div className="text-center mb-4 p-3 bg-dark rounded">

//                       <div className="text-muted small mb-2">Now Serving</div>

//                       {currentPatient ? (

//                         <div>

//                           <div className="display-4 fw-bold text-info mb-2">

//                             {currentPatient.queueNumber}

//                           </div>

//                           <div className="h5 text-white">

//                             {currentPatient.name}

//                           </div>

//                         </div>

//                       ) : (

//                         <div className="display-4 fw-bold text-muted">

//                           --

//                         </div>

//                       )}

//                     </div>


 

//                     {/* Next in Queue */}

//                     {nextPatients.length > 0 && (

//                       <div>

//                         <h5 className="text-info mb-3">Next in Queue:</h5>

//                         <div className="list-group list-group-flush">

//                           {nextPatients.map((patient, index) => (

//                             <div key={patient.id} className="list-group-item bg-dark text-white d-flex justify-content-between align-items-center">

//                               <div>

//                                 <span className="badge bg-primary me-2">{index + 1}</span>

//                                 <span className="fw-bold">{patient.queueNumber}</span>

//                                 <span className="ms-2">{patient.name}</span>

//                               </div>

//                               <small className="text-muted">

//                                 {new Date(patient.timestamp).toLocaleTimeString()}

//                               </small>

//                             </div>

//                           ))}

//                         </div>

//                       </div>

//                     )}


 

//                     {nextPatients.length === 0 && !currentPatient && (

//                       <div className="text-center text-muted py-3">

//                         <i className="fas fa-check-circle fa-3x mb-2"></i>

//                         <p>No patients in queue</p>

//                       </div>

//                     )}

//                   </div>

//                 </div>

//               </div>

//             );

//           })}

//         </div>

//       </div>


 

//       {/* Footer */}

//       <div className="text-center mt-5 text-muted">

//         <p className="mb-0">Please wait for your token to be called</p>

//         <small>Screen updates automatically</small>

//       </div>


 

//       <style>{`

//         .bg-gradient {

//           background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);

//         }


 

//         .display-4 {

//           font-size: 2.5rem;

//         }


 

//         @keyframes pulse {

//           0% { opacity: 1; }

//           50% { opacity: 0.7; }

//           100% { opacity: 1; }

//         }


 

//         .text-info {

//           animation: pulse 2s infinite;

//         }

//       `}</style>

//     </div>

//   );

// };


 

// export default NowServing;




 

import React, { useState, useEffect } from 'react';


 

import { Patient, Doctor } from './ReceptionistDashboard';





 

interface NowServingProps {


 

  patients: Patient[];


 

  doctors: Doctor[];


 

}





 

const NowServing: React.FC<NowServingProps> = ({ patients, doctors }) => {


 

  const [currentTime, setCurrentTime] = useState(new Date());





 

  // --- LOGIC (STRICTLY UNTOUCHED) ---


 

  useEffect(() => {


 

    const timer = setInterval(() => {


 

      setCurrentTime(new Date());


 

    }, 1000);


 

    return () => clearInterval(timer);


 

  }, []);





 

  const getPatientsByDoctorAndStatus = (doctorId?: string, status?: Patient['status']) => {


 

    return patients.filter(patient =>


 

      patient.doctor === doctorId &&


 

      (!status || patient.status === status)


 

    ).sort((a, b) => a.position - b.position);


 

  };





 

  const getCurrentServing = (doctorId?: string) => {


 

    const inConsultation = getPatientsByDoctorAndStatus(doctorId, 'IN_CONSULTATION');


 

    if (inConsultation.length > 0) return inConsultation[0];


 

    const waiting = getPatientsByDoctorAndStatus(doctorId, 'WAITING');


 

    return waiting.length > 0 ? waiting[0] : null;


 

  };





 

  const getNextPatients = (doctorId?: string, currentPatientId?: string) => {


 

    const waiting = getPatientsByDoctorAndStatus(doctorId, 'WAITING');


 

    return waiting


 

      .filter(p => p.id !== currentPatientId)


 

      .slice(0, 3);


 

  };





 

  return (


 

    <div className="min-vh-100 p-4"


 

      style={{


 

        background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)',


 

        color: '#212529',


 

        fontFamily: "'Segoe UI', Roboto, sans-serif"


 

      }}>




 

      <style>


 

        {`


 

          @keyframes pulse-live {


 

            0% { opacity: 1; transform: scale(1); }


 

            50% { opacity: 0.7; transform: scale(1.05); }


 

            100% { opacity: 1; transform: scale(1); }


 

          }


 

          .glass-panel-serving {


 

            background: rgba(255, 255, 255, 0.7);


 

            backdrop-filter: blur(30px);


 

            border: 1px solid rgba(0, 0, 0, 0.05);


 

            border-radius: 32px;


 

            box-shadow: 0 20px 40px rgba(0,0,0,0.04);


 

            transition: transform 0.3s ease;


 

          }


 

          .serving-number {


 

            font-size: 3.5rem;


 

            background: linear-gradient(135deg, #f2994a, #f2c94c);


 

            -webkit-background-clip: text;


 

            -webkit-text-fill-color: transparent;


 

            font-weight: 800;


 

          }


 

          .pulse-dot {


 

            width: 8px;


 

            height: 8px;


 

            background: #20c997;


 

            border-radius: 50%;


 

            display: inline-block;


 

            margin-right: 8px;


 

            animation: pulse-live 2s infinite;


 

          }


 

          .next-inline-row {


 

            border-bottom: 1px dashed #dee2e6;


 

          }


 

          .next-inline-row:last-child {


 

            border-bottom: none;


 

          }


 

        `}


 

      </style>





 

      {/* Header Area */}


 

      <div className="text-center mb-5">


 

        <div className="d-inline-block px-4 py-2 rounded-pill bg-white shadow-sm mb-3 border">


 

          <span className="pulse-dot"></span>


 

          <span className="small fw-bold text-uppercase tracking-wider text-muted">Live Queue Feed</span>


 

        </div>


 

        <h1 className="display-4 fw-bold mb-1" style={{ color: '#111', letterSpacing: '-1px' }}>Now Serving</h1>


 

        <div className="lead fw-bold text-secondary opacity-75">


 

          {currentTime.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' })} • {currentTime.toLocaleTimeString()}


 

        </div>


 

      </div>





 

      {/* Doctor Grid */}


 

      <div className="container-fluid">


 

        <div className="row g-4 justify-content-center">


 

          {doctors.map((doctor) => {


 

            const currentPatient = getCurrentServing(doctor.id);


 

            const nextPatients = getNextPatients(doctor.id, currentPatient?.id);




 

            return (


 

              <div key={doctor.id} className="col-lg-6 col-xl-4">


 

                <div className="glass-panel-serving h-100 overflow-hidden">


 

                  {/* Doctor Header */}


 

                  <div className="p-4 text-center border-bottom bg-white bg-opacity-50">


 

                    <h4 className="fw-bold mb-0" style={{ color: '#111' }}>{doctor.name}</h4>


 

                    <small className="text-uppercase fw-bold text-primary tracking-widest" style={{ fontSize: '10px' }}>


 

                      {doctor.department}


 

                    </small>


 

                  </div>





 

                  <div className="p-4">


 

                    {/* Active Token Display */}


 

                    <div className="text-center mb-4 p-4 rounded-4"


 

                      style={{ background: 'rgba(242, 153, 74, 0.05)', border: '1px solid rgba(242, 153, 74, 0.1)' }}>


 

                      <div className="small fw-bold text-uppercase text-muted mb-2 ls-1">Current Ticket</div>


 

                      {currentPatient ? (


 

                        <div>


 

                          <div className="serving-number mb-1">


 

                            {currentPatient.queueNumber}


 

                          </div>


 

                          <div className="h5 fw-bold text-dark opacity-75">


 

                            {currentPatient.name}


 

                          </div>


 

                        </div>


 

                      ) : (


 

                        <div className="serving-number text-muted opacity-25">--</div>


 

                      )}


 

                    </div>





 

                    {/* Next in Queue List */}


 

                    <div className="px-2">


 

                      <h6 className="fw-bold text-muted small text-uppercase mb-3 tracking-wider">Next in Line</h6>


 

                      {nextPatients.length > 0 ? (


 

                        <div className="rounded-4 overflow-hidden border bg-white bg-opacity-40">


 

                          {nextPatients.map((patient, index) => (


 

                            <div key={patient.id} className="next-inline-row p-3 d-flex justify-content-between align-items-center">


 

                              <div className="d-flex align-items-center">


 

                                <span className="badge bg-light text-dark rounded-circle me-3 d-flex align-items-center justify-content-center"


 

                                  style={{ width: '24px', height: '24px', fontSize: '10px' }}>


 

                                  {index + 1}


 

                                </span>


 

                                <div>


 

                                  <div className="fw-bold small" style={{ color: '#2c3e50' }}>{patient.name}</div>


 

                                  <div className="fw-bold text-primary" style={{ fontSize: '11px' }}>{patient.queueNumber}</div>


 

                                </div>


 

                              </div>


 

                              <small className="text-muted fw-bold" style={{ fontSize: '10px' }}>


 

                                {new Date(patient.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}


 

                              </small>


 

                            </div>


 

                          ))}


 

                        </div>


 

                      ) : (


 

                        <div className="text-center py-4 border rounded-4 border-dashed bg-light bg-opacity-50">


 

                          <span className="text-muted small italic">No pending queue</span>


 

                        </div>


 

                      )}


 

                    </div>


 

                  </div>


 

                </div>


 

              </div>


 

            );


 

          })}


 

        </div>


 

      </div>





 

      {/* Footer Disclaimer */}


 

      <div className="text-center mt-5">


 

        <p className="text-muted small fw-bold mb-0">Please remain in the waiting area until your token is called.</p>


 

        <small className="text-muted opacity-50 text-uppercase tracking-tighter" style={{ fontSize: '9px' }}>


 

          ClinicQ Automated Intelligent Queue System


 

        </small>


 

      </div>


 

    </div>


 

  );


 

};





 

export default NowServing;





 