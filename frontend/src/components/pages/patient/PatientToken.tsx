import React, { useState, useEffect } from 'react';

import { FaCheckCircle, FaHourglassHalf } from 'react-icons/fa';

import axiosInstance from '../../../services/axiosInstance';

import { useNavigate } from 'react-router-dom';

interface TokenProps {

  onNavigate: (tab: 'book' | 'my' | 'history' | 'token') => void;

}


 

// mapping the keys exactly to matching response attributes

interface TokenData {

   patientId: string;

   name: string;

   tokenDisplay: string;

   position: number;

   status: 'BOOKED' | 'WAITING' | 'IN_CONSULTATION' | 'COMPLETED';

   doctorName: string;

   department: string;

   estimatedWaitTime: number;

   timestamp: string;

}


 

const PatientToken: React.FC<TokenProps> = ({ onNavigate }) => {

    const navigate=useNavigate();

   const [tokenData, setTokenData] = useState<TokenData | null>(null);

   const [loading, setLoading] = useState<boolean>(true);

   const [error, setError] = useState<string | null>(null);


 

   // Replace with dynamic phone number context data if applicable


 

   useEffect(() => {

    const token=localStorage.getItem("token");

    if(!token){

        setError("Please log in to view your queue status.");

        setLoading(false);

        return;

    }

       const fetchTokenStatus = async () => {

           try {

            if(loading) setLoading(true);

           

              // setLoading(true);

               const response = await axiosInstance.get(`/clinicq/patient/queue-position`);

               setTokenData(response.data);

               setError(null);

           } catch (err: any) {

               console.error("Queue positioning data fetch error:", err);

               setError("No active consultation token tracking information found.");

           } finally {

               setLoading(false);

           }

       };

       fetchTokenStatus();

       // Polling interval: Auto updates queue position numbers every 15 seconds

       const pollInterval = setInterval(fetchTokenStatus, 15000);

       return () => clearInterval(pollInterval);

   }, []);


 

   const steps = [

       { label: 'Booked', status: 'BOOKED', icon: '📅' },

       { label: 'Waiting', status: 'WAITING', icon: '⏳' },

       { label: 'In-Consultation', status: 'IN_CONSULTATION', icon: '👨‍⚕️' },

       { label: 'Completed', status: 'COMPLETED', icon: '✅' }

   ];


 

   // Read live baseline status or default to dynamic waiting view structures

   const currentStatus = tokenData?.status || 'WAITING';

   const getStatusIndex = (status: string) => steps.findIndex(s => s.status === status);

   const currentIndex = getStatusIndex(currentStatus);


 

   return (

       <div className="d-flex min-vh-100 overflow-hidden" style={{ background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)', color: '#212529', fontFamily: "'Segoe UI', Roboto, sans-serif" }}>

           <style>

               {`

               .glass-card-token { background: rgba(255, 255, 255, 0.7); backdrop-filter: blur(25px); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); }

               .step-dot { width: 40px; height: 40px; border-radius: 50%; display: flex; align-items: center; justify-content: center; z-index: 2; transition: all 0.4s ease; font-size: 1.1rem; box-shadow: 0 4px 10px rgba(0,0,0,0.05); }

               .step-line { position: absolute; top: 20px; height: 4px; background: #dee2e6; z-index: 1; transition: all 0.4s ease; }

               .active-pulse { animation: pulseGlow 2s infinite; background: #ff7e5f !important; color: white !important; }

               @keyframes pulseGlow { 0% { box-shadow: 0 0 0 0 rgba(255, 126, 95, 0.4); } 70% { box-shadow: 0 0 0 12px rgba(255, 126, 95, 0); } 100% { box-shadow: 0 0 0 0 rgba(255, 126, 95, 0); } }

               .logout-btn:hover { background:rgba(220,53,69,0.1) !important; color: #dc3545 !important; }

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

                   <button onClick={() => onNavigate('history')} className="btn text-start py-2 px-3 border-0 text-muted fw-medium">🕒 History</button>

                   <button onClick={() => onNavigate('token')} className="btn text-start py-2 px-3 border-0 fw-bold" style={{ background: 'rgba(255, 126, 95, 0.1)', color: '#ff7e5f', borderRadius: '12px' }}>🎟️ Token No</button>

                   

                   <div className="mt-auto pt-4 border-top">

                       <button onClick={() => {localStorage.removeItem("token"); navigate('/');}} className="btn w-100 d-flex align-items-center gap-2 fw-bold logout-btn text-danger border-0 py-2 style={{fontSize:'13px'}}" ><span>📤</span> Log Out</button>

                   </div>

               </div>

           </div>


 

           {/* MAIN CONTENT AREA */}

           <div className="flex-grow-1 p-4 overflow-auto">

               <h3 className="fw-bold mb-4">Queue Status</h3>          

               <div className="row justify-content-center mt-2">

                   <div className="col-lg-8">

                       {loading ? (

                           <div className="text-center py-5">

                               <div className="spinner-border text-warning" role="status"></div>

                               <p className="mt-2 fw-bold text-muted">Retrieving live queue position data...</p>

                           </div>

                       ) : error ? (

                           <div className="glass-card-token p-5 text-center">

                               <span className="fs-1">🎟️</span>

                               <h5 className="fw-bold mt-3 text-muted">{error}</h5>

                               <p className="small text-muted">Book an intake check-in or visit registration desk to retrieve a token status journey.</p>

                           </div>

                       ) : (

                           <>

                               {/* THE TICKET */}

                               <div className="glass-card-token  shadow-lg overflow-hidden position-relative mb-4">

                                   <div style={{ height: '8px', background: 'linear-gradient(90deg, #ff7e5f, #ff6b6b)' }}></div>

                                   <div className="p-5 text-center">

                                       <span className="badge rounded-pill px-3 py-2 mb-3" style={{ background: 'rgba(255, 126, 95, 0.1)', color: '#ff7e5f', fontWeight: '700', fontSize: '15px' }}>

                                           PATIENT NAME : {tokenData?.name.toUpperCase()}

                                       </span>

                                       <h1 className="display-3 fw-bold mb-0" style={{ color: '#111', letterSpacing: '-1px' }}>

                                           {tokenData?.tokenDisplay}

                                       </h1>

                                       

                                       {/* QUEUE POSITION STATS */}

                                       <div className="d-flex justify-content-center align-items-center gap-3 mt-3">

                                           <div className="badge bg-dark px-3 py-2 rounded-pill small" style={{fontSize: '15px' }}>

                                               Position No : {tokenData?.position}

                                           </div>

                                           <div className="badge bg-warning-subtle text-warning-emphasis px-3 py-2 rounded-pill small d-flex align-items-center gap-1">

                                               <FaHourglassHalf style={{fontSize: '19px'}} /> Est. Wait Time: {tokenData?.estimatedWaitTime} mins

                                           </div>

                                       </div>


 

                                       <p className="text-muted small mt-3"></p>

                                       

                                       <div className="d-flex justify-content-center gap-4 border-top mt-4 pt-4">

                                           <div className="text-start">

                                               <small className="text-muted fw-bold d-block text-uppercase" style={{ fontSize: '9px' }}>Doctor</small>

                                               <span className="fw-bold">{tokenData?.doctorName}</span>

                                           </div>

                                           <div className="vr"></div>

                                           <div className="text-start">

                                               <small className="text-muted fw-bold d-block text-uppercase" style={{ fontSize: '9px' }}>Department</small>

                                               <span className="fw-bold">{tokenData?.department}</span>

                                           </div>

                                       </div>

                                   </div>

                               </div>


 

                               {/* THE TIMELINE */}

                               <div className="glass-card-token p-5 shadow-sm">

                                   <h6 className="fw-bold text-muted small text-uppercase mb-4">Visit Journey</h6>

                                   

                                   <div className="position-relative d-flex justify-content-between align-items-center px-2">

                                       <div className="step-line w-100"></div>

                                       <div className="step-line" style={{ width: currentIndex >= 0 ? `${(currentIndex / (steps.length - 1)) * 100}%` : '0%', background: '#ff7e5f' }}></div>


 

                                       {steps.map((step, index) => {

                                           const isCompleted = index < currentIndex;

                                           const isActive = index === currentIndex;

                                           

                                           return (

                                               <div key={step.status} className="d-flex flex-column align-items-center position-relative" style={{ zIndex: 2 }}>

                                                   <div className={`step-dot ${isActive ? 'active-pulse' : ''}`} 

                                                        style={{ 

                                                            background: isCompleted ? '#ff7e5f' : '#fff', 

                                                            color: isCompleted ? '#fff' : '#6c757d',

                                                            border: isCompleted || isActive ? 'none' : '2px solid #dee2e6' 

                                                        }}>

                                                       {isCompleted ? <FaCheckCircle /> : step.icon}

                                                   </div>

                                                   <span className={`mt-3 small fw-bold ${isActive ? 'text-dark' : 'text-muted'}`} 

                                                         style={{ position: 'absolute', top: '40px', whiteSpace: 'nowrap', fontSize: '10px' }}>

                                                       {step.label}

                                                   </span>

                                               </div>

                                           );

                                       })}

                                   </div>

                               </div>

                           </>

                       )}

                   </div>

               </div>

           </div>

       </div>

   );

};


 

export default PatientToken;