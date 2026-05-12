// import React, { useState } from 'react';

// // Import Admin Pages

// import AdminDashboard from './components/pages/admin/AdminDashboard';

// // Import Auth Pages

// import Login from './components/pages/auth/Login';

// import PatientLogin from './components/pages/auth/PatientLogin';

// import PatientRegister from './components/pages/auth/PatientRegister';

// // Import Patient Pages

// import BookAppointment from './components/pages/patient/BookAppointment';

// import MyAppointments from './components/pages/patient/MyAppointments';

// import PastHistory from './components/pages/patient/PastHistory';

// import ReceptionistLogin from './components/pages/receptionist/ReceptionistLogin';

// import ReceptionistDashboard from './components/pages/receptionist/ReceptionistDashboard';

// import DoctorLogin from './components/pages/doctor/DoctorLogin';

// import DoctorDashboard from './components/pages/doctor/DoctorDashboard';

// import NowServing from './components/pages/receptionist/NowServing';

// import { FaUser, FaLaptop, FaStethoscope, FaUserInjured, FaPhoneAlt, FaThermometer, FaClinicMedical, FaHome } from 'react-icons/fa';

// function App() {

//   const [view, setView] = useState<'home' | 'login' | 'patientLogin' | 'patientRegister' | 'admin' | 'patient' | 'receptionistLogin' | 'receptionist' | 'doctorLogin' | 'doctor'>('home');

//   const [loginRole, setLoginRole] = useState('');

//   const [isAdminFormOpen, setIsAdminFormOpen] = useState(false);

//   // Patient Dashboard Sub-tabs

//   const [patientSubView, setPatientSubView] = useState<'book' | 'my' | 'history'>('book');


 

//   // --- 1. THE WELCOME PAGE (Selection) ---

//   if (view === 'home') {

//     return (

//       <div className="bg-black min-vh-100 d-flex flex-column align-items-center justify-content-center text-white">

//         <div className="text-center mb-5">

//           <div className="bg-info rounded-circle mx-auto mb-3 shadow-lg d-flex align-items-center justify-content-center"

//               style={{ width: '100px', height: '100px' }}>

//             <FaClinicMedical style={{ fontSize:'65px',color:'#000000' }}/>

//           </div>

//           <h1 className="display-3 fw-bold text-info">

//             ClinicQ

//           </h1>

//           <p className="text-secondary">Please select your portal to continue</p>

//         </div>


 

//         <div className="row g-4 container" style={{ maxWidth: '800px' }}>

//           {/* Admin Button -> Goes to Login */}

//           <div className="col-12 col-md-6">

//             <button

//               onClick={() => { setLoginRole('Admin'); setView('login'); }}

//               className="btn btn-outline-info w-100 py-4 fs-4 fw-bold shadow-lg border-2"

//             >

//               {/*👨‍💻 Admin Portal*/}

//               <FaUser style={{ marginRight: '8px', marginBottom: '9px' }} /> Admin Portal

//             </button>

//           </div>


 

//           {/* Patient Button -> NOW GOES TO PATIENT LOGIN (Corrected) */}

//           <div className="col-12 col-md-6">

//             <button

//               onClick={() => setView('patientLogin')}

//               className="btn btn-outline-primary w-100 py-4 fs-4 fw-bold shadow-lg border-2"

//             >

//               {/*🤒 Patient Portal*/}

//               <FaUserInjured style={{ marginRight: '8px', marginBottom: '9px' }} />Patient Portal

//             </button>

//           </div>


 

//           <div className="col-12 col-md-6">

//             <button onClick={() => setView('doctorLogin')} className="btn btn-outline-success w-100 py-4 fs-4 fw-bold shadow-lg border-2">

//               {/*🩺 General Doctor*/}

//               <FaStethoscope style={{ marginRight: '8px', marginBottom: '9px' }} />General Doctor

//             </button>

//           </div>


 

//           {/* Replace the current Receptionist button block with this */}

//           <div className="col-12 col-md-6">

//             <button

//               onClick={() => setView('receptionistLogin')}

//               className="btn btn-outline-warning w-100 py-4 fs-4 fw-bold shadow-lg border-2"

//             >

//               {/*📞 Receptionist*/}

//               <FaPhoneAlt style={{ marginRight: '8px', marginBottom: '9px' }}/> Receptionist

//             </button>

//           </div>

//         </div>

//       </div>

//     );

//   }



//   // --- 2. AUTH VIEWS ---


 

//   // Admin Login

//   if (view === 'login') {

//     return <Login onLogin={() => setView('admin')} />;

//   }


 

//   // Patient Login

//   if (view === 'patientLogin') {

//     return (

//       <PatientLogin

//         onLogin={() => setView('patient')}

//         onGoToRegister={() => setView('patientRegister')}

//         onReturnHome={() => setView('home')}

//       />

//     );

//   }


 

//   // Patient Registration

//   if (view === 'patientRegister') {

//     return (

//       <PatientRegister

//         onRegisterSuccess={() => { alert("Account Created! Please Sign In."); setView('patientLogin'); }}

//         onReturnToLogin={() => setView('patientLogin')}

//       />

//     );

//   }

//   // Doctor Login View

//   if (view === 'doctorLogin') {

//     return (

//       <DoctorLogin

//         onLogin={() => setView('doctor')}

//       />

//     );

//   }

//   // --- 3. DASHBOARD VIEWS ---


 

//   // Admin Dashboard

//   if (view === 'admin') {

//     return (

//       <>

//         {/* Now we check our local state here */}

//         {!isAdminFormOpen && (

//           <button

//             onClick={() => setView('home')}

//             className="btn btn-sm btn-secondary position-fixed top-0 end-0 m-2"

//             style={{ zIndex: 3000 }}

//           >

//             {/*🏠 Back to Home*/}

//             <FaHome style={{ marginRight: '3px', marginBottom: '5px' }} /> Back to Home

//           </button>

//         )}


 

//         {/* Pass the function to the Dashboard */}

//         <AdminDashboard onToggleForm={(isOpen) => setIsAdminFormOpen(isOpen)} />

//       </>

//     );

//   }

//   // Patient Dashboard (Tabs: Book, My Appts, History)

//   if (view === 'patient') {

//     return (

//       <>

//         <button onClick={() => setView('home')} className="btn btn-sm btn-secondary position-fixed bottom-0 start-0 m-2" style={{ zIndex: 3000 }}>🏠 Exit Portal</button>

//         {patientSubView === 'book' && <BookAppointment onNavigate={setPatientSubView} />}


 

//         {patientSubView === 'my' && <MyAppointments onNavigate={setPatientSubView} onReturnHome={() => setView('home')} />}

//         {patientSubView === 'history' && <PastHistory onNavigate={setPatientSubView} />}

//       </>

//     );

//   }


 

//   // --- RECEPTIONIST VIEWS ---


 

//   // Receptionist Login

//   if (view === 'receptionistLogin') {

//     return (

//       <ReceptionistLogin

//         onLogin={() => setView('receptionist')}

//       />

//     );

//   }


 

//   // NEW: Doctor Dashboard View (User Story 07)

//   if (view === 'doctor') {

//     return (

//       <>

//         <button

//           onClick={() => setView('home')}

//           className="btn btn-sm btn-secondary position-fixed bottom-0 start-0 m-2"

//           style={{ zIndex: 3000 }}

//         >

//           {/*🏠 Exit Dashboard*/}

//           <FaHome style={{ marginRight: '3px', marginBottom: '5px' }} /> Exit Dashboard

//         </button>

//         <DoctorDashboard />

//       </>

//     );

//   }


 

//   // Receptionist Dashboard (User Story 04)

//   if (view === 'receptionist') {

//     return (

//       <>

//         {/* Small Exit button for development convenience */}

//         <button

//           onClick={() => setView('home')}

//           className="btn btn-sm btn-secondary position-fixed bottom-0 start-0 m-2"

//           style={{ zIndex: 3000 }}

//         >

//           {/*🏠 Exit Console*/}

//           <FaHome style={{ marginRight: '3px', marginBottom: '5px' }} /> Exit Console

//         </button>


 

//         <ReceptionistDashboard />



 

//       </>

//     );

//   }


 

//   return null;

// }


 

// export default App;


 

import React, { useState } from 'react';


 

// Import Admin Pages


 

import AdminDashboard from './components/pages/admin/AdminDashboard';


 

// Import Auth Pages


 

import Login from './components/pages/auth/Login';


 

import PatientLogin from './components/pages/auth/PatientLogin';


 

import PatientRegister from './components/pages/auth/PatientRegister';


 

// Import Patient Pages


 

import BookAppointment from './components/pages/patient/BookAppointment';


 

import MyAppointments from './components/pages/patient/MyAppointments';


 

import PastHistory from './components/pages/patient/PastHistory';


 

// Import Staff Pages


 

import ReceptionistLogin from './components/pages/receptionist/ReceptionistLogin';


 

import ReceptionistDashboard from './components/pages/receptionist/ReceptionistDashboard';


 

import DoctorLogin from './components/pages/doctor/DoctorLogin';


 

import DoctorDashboard from './components/pages/doctor/DoctorDashboard';



 




 

import { FaUser, FaStethoscope, FaUserInjured, FaPhoneAlt, FaClinicMedical, FaHome } from 'react-icons/fa';


 

import Swal from 'sweetalert2';


 

function App() {


 

  // --- Navigation State (Untouched) ---


 

  const [view, setView] = useState<'home' | 'login' | 'patientLogin' | 'patientRegister' | 'admin' | 'patient' | 'receptionistLogin' | 'receptionist' | 'doctorLogin' | 'doctor'>('home');


 

  const [loginRole, setLoginRole] = useState('');


 

  const [isAdminFormOpen, setIsAdminFormOpen] = useState(false);


 

  const [patientSubView, setPatientSubView] = useState<'book' | 'my' | 'history'>('book');



 




 

  // --- 1. THE NEW LIGHT THEME WELCOME PAGE ---


 

  if (view === 'home') {


 

    return (


 

      <div style={{


 

        minHeight: '100vh',


 

        background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)',


 

        fontFamily: "'Segoe UI', Roboto, sans-serif",


 

        color: '#212529',


 

        overflowX: 'hidden'


 

      }}>


 

        {/* Header */}


 

        <nav className="container d-flex justify-content-between align-items-center py-4">


 

          <div className="fs-3 fw-bold d-flex align-items-center">


 

            <span className="text-white rounded px-2 me-2 d-flex align-items-center justify-content-center"


 

              style={{ width: '40px', height: '40px', background: '#ff7e5f' }}>


 

              <FaClinicMedical style={{ fontSize: '1.2rem' }} />


 

            </span>


 

            ClinicQ


 

          </div>


 

        </nav>



 




 

        {/* Hero Section */}


 

        <div className="container text-center py-1">


 

          <h1 className="display-4 fw-bold mb-3" style={{ letterSpacing: '-1px', color: '#111' }}>


 

            Optimize Your Clinic with ClinicQ


 

          </h1>


 

          <p className="lead mb-5 mx-auto opacity-75" style={{ maxWidth: '700px' }}>


 

            A comprehensive, single-page management platform designed specifically for modern healthcare providers and their patients.


 

          </p>



 




 

          {/* Portal Selection Cards */}


 

          <div className="row g-4 mb-5 justify-content-center">


 

            {[


 

              {


 

                label: 'Admin Portal', desc: 'System settings & roles', icon: <FaUser />,


 

                border: '#6a11cb',


 

                action: () => { setLoginRole('Admin'); setView('login'); }


 

              },


 

              {


 

                label: 'Patient Portal', desc: 'Book & view records', icon: <FaUserInjured />,


 

                border: '#ff7e5f',


 

                action: () => setView('patientLogin')


 

              },


 

              {


 

                label: 'General Doctor', desc: 'Clinical consultations', icon: <FaStethoscope />,


 

                border: '#0beacb',


 

                action: () => setView('doctorLogin')


 

              },


 

              {


 

                label: 'Receptionist', desc: 'Coordinate schedules', icon: <FaPhoneAlt />,


 

                border: '#f2994a',


 

                action: () => setView('receptionistLogin')


 

              }


 

            ].map((portal, i) => (


 

              <div key={i} className="col-12 col-sm-6 col-lg-3">


 

                <div className="p-4 h-100 text-start shadow-sm d-flex flex-column"


 

                  style={{


 

                    background: '#fff',


 

                    borderRadius: '28px',


 

                    border: `1px solid #eee`,


 

                    borderTop: `5px solid ${portal.border}`,


 

                    transition: 'all 0.3s ease'


 

                  }}


 

                  onMouseOver={(e) => {


 

                    e.currentTarget.style.transform = 'translateY(-10px)';


 

                    e.currentTarget.style.boxShadow = '0 15px 30px rgba(0,0,0,0.1)';


 

                  }}


 

                  onMouseOut={(e) => {


 

                    e.currentTarget.style.transform = 'translateY(0)';


 

                    e.currentTarget.style.boxShadow = 'none';


 

                  }}


 

                >


 

                  <div className="fs-2 mb-3" style={{ color: portal.border }}>{portal.icon}</div>


 

                  <h5 className="fw-bold mb-2" style={{ color: '#111' }}>{portal.label}</h5>


 

                  <p className="small text-muted mb-4">{portal.desc}</p>


 

                  <button


 

                    onClick={portal.action}


 

                    className="btn w-100 fw-bold rounded-pill mt-auto"


 

                    style={{ background: portal.border, color: '#fff', fontSize: '0.85rem', padding: '10px' }}


 

                  >


 

                    Enter Portal →


 

                  </button>


 

                </div>


 

              </div>


 

            ))}


 

          </div>



 




 

          {/* --- ADDED: LIGHT GLASSMORPHISM ANALYTICS DASHBOARD --- */}


 

          <div className="rounded-5 p-4 shadow-sm mx-auto border mt-5"


 

            style={{


 

              maxWidth: '1100px',


 

              background: 'rgba(255, 255, 255, 0.7)',


 

              backdropFilter: 'blur(30px)',


 

              borderColor: 'rgba(0, 0, 0, 0.05)',


 

              borderRadius: '40px'


 

            }}>



 




 

            <style>


 

              {`


 

                @keyframes waveDraw { from { stroke-dashoffset: 400; fill-opacity: 0; } to { stroke-dashoffset: 0; fill-opacity: 0.1; } }


 

                @keyframes growRight { from { width: 0%; } to { width: 75%; } }


 

                .wave-path { stroke-dasharray: 400; animation: waveDraw 2.5s ease-out forwards; }


 

                .progress-fill { animation: growRight 1.5s ease-out forwards; box-shadow: 0 2px 8px rgba(0, 123, 255, 0.2); }


 

              `}


 

            </style>



 




 

            <div className="d-flex justify-content-between align-items-center mb-4 px-3">


 

              <h5 className="fw-bold m-0" style={{ color: '#007bff', letterSpacing: '0.5px' }}>


 

                Analytics at a Glance


 

              </h5>


 

              <span className="badge bg-success-subtle text-success rounded-pill px-3 border border-success border-opacity-10" style={{ fontSize: '0.75rem' }}>


 

                ● Real-time


 

              </span>


 

            </div>



 




 

            <div className="row g-4 px-2">


 

              {/* 1. Patient Visits (WAVE CHART) */}


 

              <div className="col-md-4">


 

                <div className="p-3 border rounded-4 text-start h-100" style={{ background: '#fff', borderColor: '#eee' }}>


 

                  <span style={{ color: '#6c757d', fontSize: '0.8rem' }}>Patient Visits</span>


 

                  <div className="mt-2 position-relative" style={{ height: '80px' }}>


 

                    <svg viewBox="0 0 200 80" className="w-100 h-100">


 

                      <path className="wave-path" d="M0,60 Q25,10 50,45 T100,30 T150,50 T200,20 L200,80 L0,80 Z" fill="#007bff" stroke="#007bff" strokeWidth="2" fillOpacity="0.1" />


 

                    </svg>


 

                    <h4 className="fw-bold position-absolute bottom-0 start-0 m-0" style={{ color: '#212529' }}>120</h4>


 

                  </div>


 

                  <div className="d-flex justify-content-between mt-2" style={{ fontSize: '0.6rem', color: '#adb5bd' }}>


 

                    <span>Sun</span><span>Mon</span><span>Tue</span><span>Wed</span><span>Thu</span><span>Fri</span><span>Sat</span>


 

                  </div>


 

                </div>


 

              </div>



 




 

              {/* 2. Specialty Performance */}


 

              <div className="col-md-4">


 

                <div className="p-3 border rounded-4 text-start h-100" style={{ background: '#fff', borderColor: '#eee' }}>


 

                  <span style={{ color: '#6c757d', fontSize: '0.85rem' }}>Specialty Performance</span>


 

                  <div className="d-flex mt-3 flex-grow-1" style={{ minHeight: '120px' }}>


 

                    <div className="d-flex flex-column w-100">


 

                      <div className="d-flex gap-2 align-items-end justify-content-around flex-grow-1 px-2">


 

                        <div style={{ width: '18%', height: '40%', background: '#e83e8c', borderRadius: '4px 4px 0 0' }}></div>


 

                        <div style={{ width: '18%', height: '80%', background: '#007bff', borderRadius: '4px 4px 0 0' }}></div>


 

                        <div style={{ width: '18%', height: '30%', background: '#fd7e14', borderRadius: '4px 4px 0 0' }}></div>


 

                        <div style={{ width: '18%', height: '60%', background: '#20c997', borderRadius: '4px 4px 0 0' }}></div>


 

                      </div>


 

                      <div style={{ borderTop: '1px solid #eee', width: '100%' }}></div>


 

                      <div className="d-flex justify-content-around pt-2 ps-2" style={{ fontSize: '0.65rem', color: '#6c757d', fontWeight: '500' }}>


 

                        <span>GEN</span><span>CARD</span><span>ORTH</span><span>PED</span>


 

                      </div>


 

                    </div>


 

                  </div>


 

                </div>


 

              </div>



 




 

              {/* 3. Today's Appointments (DONUT CHART) */}


 

              <div className="col-md-4">


 

                <div className="p-4 rounded-4 border h-100" style={{ background: 'rgba(0, 123, 255, 0.03)', borderColor: 'rgba(0, 123, 255, 0.1)', display: 'flex', flexDirection: 'column', justifyContent: 'center', minHeight: '180px' }}>


 

                  <div className="text-center mb-3">


 

                    <span style={{ color: '#212529', fontSize: '0.9rem', fontWeight: '500' }}>Today's Appointments</span>


 

                  </div>


 

                  <div className="d-flex justify-content-center align-items-center">


 

                    <div style={{ width: '100px', height: '100px', borderRadius: '50%', background: 'conic-gradient(#007bff 0% 75%, #e9ecef 75% 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 4px 15px rgba(0,0,0,0.05)', position: 'relative' }}>


 

                      <div style={{ width: '70px', height: '70px', backgroundColor: '#fff', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.9rem', color: '#007bff', fontWeight: 'bold' }}>


 

                        75%


 

                      </div>


 

                    </div>


 

                  </div>


 

                </div>


 

              </div>


 

            </div>



 




 

            <div className="mt-4 pt-3 text-center border-top" style={{ borderColor: '#eee' }}>


 

              <span style={{ color: '#adb5bd', fontSize: '0.75rem' }}>


 

                Trusted by 🛡️ <strong>clinics worldwide</strong>.


 

              </span>


 

            </div>


 

          </div>


 

          {/* --- END ANALYTICS --- */}



 




 

        </div>


 

        <div className="py-5"></div>


 

      </div>


 

    );


 

  }



 




 

  // --- 2. AUTH VIEWS ---


 

  if (view === 'login') return <Login onLogin={() => setView('admin')} />;



 




 

  if (view === 'patientLogin') {


 

    return (


 

      <PatientLogin


 

        onLogin={() => setView('patient')}


 

        onGoToRegister={() => setView('patientRegister')}


 

        onReturnHome={() => setView('home')}


 

      />


 

    );


 

  }



 




 

  if (view === 'patientRegister') {


 

    return (


 

      <PatientRegister


 

        //onRegisterSuccess={() => { setView('patientLogin'); }}


 

        onRegisterSuccess={() => {


 

          Swal.fire('Success', 'Account Created! Please login.', 'success'); // Add this


 

          setView('patientLogin');


 

        }}


 

        onReturnToLogin={() => setView('patientLogin')}


 

      />


 

    );


 

  }



 




 

  if (view === 'doctorLogin') return <DoctorLogin onLogin={() => setView('doctor')} />;


 

  if (view === 'receptionistLogin') return <ReceptionistLogin onLogin={() => setView('receptionist')} />;



 




 

  // --- 3. DASHBOARD VIEWS ---


 

  if (view === 'admin') {


 

    return (


 

      <>


 

        {/* {!isAdminFormOpen && (


 

          // <button onClick={() => setView('home')} className="btn btn-sm btn-dark position-fixed top-0 end-0 m-3 rounded-pill px-3 shadow" style={{ zIndex: 3000 }}>


 

          //   <FaHome className="me-2" /> Exit Admin


 

          // </button>


 

        )} */}


 

        <AdminDashboard onToggleForm={(isOpen) => setIsAdminFormOpen(isOpen)} />


 

      </>


 

    );


 

  }



 




 

  if (view === 'patient') {


 

    return (


 

      <>


 

        {/* <button onClick={() => setView('home')} className="btn btn-sm btn-dark position-fixed bottom-0 start-0 m-3 rounded-pill px-3 shadow" style={{ zIndex: 3000 }}>


 

          <FaHome className="me-2" /> Exit Portal


 

        </button> */}


 

        {patientSubView === 'book' && <BookAppointment onNavigate={setPatientSubView} />}


 

        {patientSubView === 'my' && <MyAppointments onNavigate={setPatientSubView} onReturnHome={() => setView('home')} />}


 

        {patientSubView === 'history' && <PastHistory onNavigate={setPatientSubView} />}


 

      </>


 

    );


 

  }



 




 

  if (view === 'doctor') {


 

    return (


 

      <>


 

        {/* <button onClick={() => setView('home')} className="btn btn-sm btn-dark position-fixed bottom-0 start-0 m-3 rounded-pill px-3 shadow" style={{ zIndex: 3000 }}>


 

          <FaHome className="me-2" /> Exit Console


 

        </button> */}


 

        <DoctorDashboard />


 

      </>


 

    );


 

  }



 




 

  if (view === 'receptionist') {


 

    return (


 

      <>


 

        {/* <button onClick={() => setView('home')} className="btn btn-sm btn-dark position-fixed bottom-0 start-0 m-3 rounded-pill px-3 shadow" style={{ zIndex: 3000 }}>


 

          <FaHome className="me-2" /> Exit Staff Portal


 

        </button> */}


 

        <ReceptionistDashboard />


 

      </>


 

    );


 

  }



 




 

  return null;


 

}


 

export default App;