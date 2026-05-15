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


 

 // --- 1. PREMIUM LANDING PAGE ---

 if (view === 'home') {

   return (

     <div style={{

       minHeight: '100vh',

       background: '#0a0a0f',

       fontFamily: "'DM Sans', 'Segoe UI', sans-serif",

       color: '#f0f0f5',

       overflowX: 'hidden',

       position: 'relative'

     }}>

       <style>{`

         @import url('https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;0,9..40,600;0,9..40,700;1,9..40,300&family=Syne:wght@700;800&display=swap');


 

         * { box-sizing: border-box; }


 

         /* Animated gradient orbs */

         .orb {

           position: absolute;

           border-radius: 50%;

           filter: blur(120px);

           pointer-events: none;

           z-index: 0;

         }

         .orb-1 {

           width: 600px; height: 600px;

           background: radial-gradient(circle, rgba(255,126,95,0.15) 0%, transparent 70%);

           top: -200px; left: -200px;

           animation: driftOrb1 20s ease-in-out infinite;

         }

         .orb-2 {

           width: 500px; height: 500px;

           background: radial-gradient(circle, rgba(110,86,255,0.12) 0%, transparent 70%);

           top: 100px; right: -150px;

           animation: driftOrb2 25s ease-in-out infinite;

         }

         .orb-3 {

           width: 400px; height: 400px;

           background: radial-gradient(circle, rgba(32,201,151,0.1) 0%, transparent 70%);

           bottom: 200px; left: 30%;

           animation: driftOrb3 18s ease-in-out infinite;

         }

         @keyframes driftOrb1 { 0%,100%{transform:translate(0,0)} 50%{transform:translate(80px,60px)} }

         @keyframes driftOrb2 { 0%,100%{transform:translate(0,0)} 50%{transform:translate(-60px,80px)} }

         @keyframes driftOrb3 { 0%,100%{transform:translate(0,0)} 50%{transform:translate(40px,-50px)} }


 

         /* Grid texture overlay */

         .grid-overlay {

           position: absolute;

           inset: 0;

           background-image:

             linear-gradient(rgba(255,255,255,0.015) 1px, transparent 1px),

             linear-gradient(90deg, rgba(255,255,255,0.015) 1px, transparent 1px);

           background-size: 60px 60px;

           pointer-events: none;

           z-index: 0;

         }


 

         /* Noise grain */

         .noise {

           position: fixed;

           inset: -50%;

           width: 200%; height: 200%;

           background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)' opacity='0.04'/%3E%3C/svg%3E");

           pointer-events: none;

           z-index: 1;

           opacity: 0.4;

         }


 

         .content-layer { position: relative; z-index: 2; }


 

         /* Navigation */

         .nav-glass {

           background: rgba(10,10,15,0.7);

           backdrop-filter: blur(20px);

           border-bottom: 1px solid rgba(255,255,255,0.06);

         }


 

         /* Badge pill */

         .status-badge {

           display: inline-flex;

           align-items: center;

           gap: 6px;

           padding: 6px 14px;

           background: rgba(32,201,151,0.08);

           border: 1px solid rgba(32,201,151,0.2);

           border-radius: 100px;

           font-size: 11px;

           font-weight: 600;

           color: #20c997;

           letter-spacing: 0.5px;

           text-transform: uppercase;

         }

         .status-dot {

           width: 6px; height: 6px;

           background: #20c997;

           border-radius: 50%;

           animation: pulseDot 2s ease-in-out infinite;

         }

         @keyframes pulseDot {

           0%,100%{opacity:1;transform:scale(1)}

           50%{opacity:0.5;transform:scale(1.3)}

         }


 

         /* Hero heading */

         .hero-title {

           font-family: 'Syne', sans-serif;

           font-size: clamp(52px, 7vw, 96px);

           font-weight: 800;

           line-height: 0.95;

           letter-spacing: -3px;

           background: linear-gradient(135deg, #ffffff 0%, rgba(255,255,255,0.6) 100%);

           -webkit-background-clip: text;

           -webkit-text-fill-color: transparent;

           background-clip: text;

         }

         .hero-accent {

           font-family: 'Syne', sans-serif;

           font-weight: 800;

           background: linear-gradient(135deg, #ff7e5f 0%, #ff4757 50%, #c471ed 100%);

           -webkit-background-clip: text;

           -webkit-text-fill-color: transparent;

           background-clip: text;

         }


 

         /* Portal cards */

         .portal-card {

           background: rgba(255,255,255,0.03);

           border: 1px solid rgba(255,255,255,0.07);

           border-radius: 20px;

           padding: 32px 28px;

           transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);

           cursor: pointer;

           position: relative;

           overflow: hidden;

           text-decoration: none;

         }

         .portal-card::before {

           content: '';

           position: absolute;

           inset: 0;

           background: var(--card-glow);

           opacity: 0;

           transition: opacity 0.4s ease;

           border-radius: inherit;

         }

         .portal-card:hover {

           border-color: var(--card-accent);

           transform: translateY(-6px);

           background: rgba(255,255,255,0.05);

         }

         .portal-card:hover::before { opacity: 1; }


 

         .card-icon-wrap {

           width: 52px; height: 52px;

           border-radius: 14px;

           display: flex; align-items: center; justify-content: center;

           font-size: 1.4rem;

           margin-bottom: 20px;

           transition: transform 0.3s ease;

         }

         .portal-card:hover .card-icon-wrap { transform: scale(1.1) rotate(-3deg); }


 

         .card-title {

           font-family: 'Syne', sans-serif;

           font-size: 1.15rem;

           font-weight: 700;

           color: #fff;

           margin-bottom: 6px;

         }

         .card-desc {

           font-size: 0.82rem;

           color: rgba(255,255,255,0.4);

           font-weight: 400;

           line-height: 1.5;

           margin-bottom: 24px;

         }

         .card-arrow {

           display: inline-flex;

           align-items: center;

           gap: 6px;

           font-size: 0.8rem;

           font-weight: 600;

           color: var(--card-accent);

           transition: gap 0.3s ease;

         }

         .portal-card:hover .card-arrow { gap: 10px; }


 

         /* Stats strip */

         .stat-item {

           padding: 0 32px;

           border-right: 1px solid rgba(255,255,255,0.06);

         }

         .stat-item:last-child { border-right: none; }

         .stat-number {

           font-family: 'Syne', sans-serif;

           font-size: 2rem;

           font-weight: 800;

           color: #fff;

           line-height: 1;

         }

         .stat-label {

           font-size: 0.75rem;

           color: rgba(255,255,255,0.4);

           font-weight: 500;

           margin-top: 4px;

           text-transform: uppercase;

           letter-spacing: 0.5px;

         }


 

         /* Dashboard preview */

         .dashboard-preview {

           background: rgba(255,255,255,0.02);

           border: 1px solid rgba(255,255,255,0.06);

           border-radius: 24px;

           overflow: hidden;

         }

         .preview-bar {

           background: rgba(255,255,255,0.03);

           border-bottom: 1px solid rgba(255,255,255,0.05);

           padding: 12px 20px;

           display: flex;

           align-items: center;

           gap: 8px;

         }

         .traffic-dot {

           width: 10px; height: 10px;

           border-radius: 50%;

         }


 

         /* Mini metric cards in preview */

         .mini-metric {

           background: rgba(255,255,255,0.04);

           border: 1px solid rgba(255,255,255,0.06);

           border-radius: 12px;

           padding: 16px;

           flex: 1;

         }

         .mini-metric-val {

           font-family: 'Syne', sans-serif;

           font-size: 1.6rem;

           font-weight: 800;

           color: #fff;

           line-height: 1;

         }

         .mini-metric-label {

           font-size: 0.7rem;

           color: rgba(255,255,255,0.35);

           text-transform: uppercase;

           letter-spacing: 0.5px;

           margin-top: 4px;

           font-weight: 600;

         }


 

         /* Chart bar animated */

         .chart-bar {

           border-radius: 4px 4px 0 0;

           animation: growUp 1.5s cubic-bezier(0.16,1,0.3,1) forwards;

           transform-origin: bottom;

         }

         @keyframes growUp {

           from { transform: scaleY(0); }

           to { transform: scaleY(1); }

         }


 

         /* Wave SVG path */

         .wave-line {

           stroke-dasharray: 300;

           stroke-dashoffset: 300;

           animation: drawWave 2s ease-out forwards 0.5s;

         }

         @keyframes drawWave {

           to { stroke-dashoffset: 0; }

         }


 

         /* CTA Button */

         .btn-primary-glow {

           background: linear-gradient(135deg, #ff7e5f, #ff4757);

           border: none;

           border-radius: 12px;

           color: #fff;

           font-weight: 700;

           font-size: 0.9rem;

           padding: 14px 28px;

           cursor: pointer;

           transition: all 0.3s ease;

           box-shadow: 0 0 0 0 rgba(255,126,95,0.4);

           position: relative;

           overflow: hidden;

         }

         .btn-primary-glow:hover {

           transform: translateY(-2px);

           box-shadow: 0 8px 30px rgba(255,126,95,0.35);

           color: #fff;

         }

         .btn-ghost {

           background: transparent;

           border: 1px solid rgba(255,255,255,0.12);

           border-radius: 12px;

           color: rgba(255,255,255,0.7);

           font-weight: 600;

           font-size: 0.9rem;

           padding: 13px 24px;

           cursor: pointer;

           transition: all 0.3s ease;

         }

         .btn-ghost:hover {

           background: rgba(255,255,255,0.05);

           border-color: rgba(255,255,255,0.2);

           color: #fff;

         }


 

         /* Feature pills */

         .feature-pill {

           display: inline-flex;

           align-items: center;

           gap: 8px;

           padding: 8px 16px;

           background: rgba(255,255,255,0.04);

           border: 1px solid rgba(255,255,255,0.07);

           border-radius: 100px;

           font-size: 0.8rem;

           color: rgba(255,255,255,0.55);

           font-weight: 500;

         }


 

         /* Fade in animation */

         @keyframes fadeInUp {

           from { opacity: 0; transform: translateY(30px); }

           to { opacity: 1; transform: translateY(0); }

         }

         .fade-in-1 { animation: fadeInUp 0.8s cubic-bezier(0.16,1,0.3,1) forwards; }

         .fade-in-2 { animation: fadeInUp 0.8s cubic-bezier(0.16,1,0.3,1) 0.15s both; }

         .fade-in-3 { animation: fadeInUp 0.8s cubic-bezier(0.16,1,0.3,1) 0.3s both; }

         .fade-in-4 { animation: fadeInUp 0.8s cubic-bezier(0.16,1,0.3,1) 0.45s both; }


 

         .logo-mark {

           width: 36px; height: 36px;

           background: linear-gradient(135deg, #ff7e5f, #c471ed);

           border-radius: 10px;

           display: flex; align-items: center; justify-content: center;

         }


 

         /* Donut chart CSS */

         .donut-ring {

           transform: rotate(-90deg);

           transform-origin: center;

         }

         .donut-segment {

           stroke-dasharray: 75 100;

           animation: donutFill 1.5s ease-out forwards;

         }

         @keyframes donutFill {

           from { stroke-dasharray: 0 100; }

           to { stroke-dasharray: 75 100; }

         }

       `}</style>


 

       {/* Atmospheric orbs */}

       <div className="orb orb-1" />

       <div className="orb orb-2" />

       <div className="orb orb-3" />

       <div className="grid-overlay" />

       <div className="noise" />


 

       {/* ── NAVIGATION ── */}

       <nav className="nav-glass content-layer sticky-top" style={{ padding: '0 clamp(20px, 5vw, 60px)' }}>

         <div style={{ maxWidth: '1280px', margin: '0 auto', display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: '64px' }}>

           <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>

             <div className="logo-mark">

               <FaClinicMedical style={{ color: '#fff', fontSize: '1rem' }} />

             </div>

             <span style={{ fontFamily: 'Syne, sans-serif', fontWeight: 800, fontSize: '1.1rem', color: '#fff', letterSpacing: '-0.5px' }}>ClinicQ</span>

           </div>

           <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>

             <div className="status-badge">

               <div className="status-dot" />

               System Operational

             </div>

           </div>

         </div>

       </nav>


 

       {/* ── HERO SECTION ── */}

       <div className="content-layer" style={{ maxWidth: '1280px', margin: '0 auto', padding: 'clamp(60px,10vh,120px) clamp(20px,5vw,60px) 0' }}>


 

         <div className="fade-in-1" style={{ marginBottom: '24px' }}>

           <span className="status-badge">

             Healthcare Management Platform

           </span>

         </div>


 

         <h1 className="hero-title fade-in-2" style={{ maxWidth: '800px', marginBottom: '24px' }}>

           Smart Clinic<br />

           <span className="hero-accent">Management</span><br />

           System

         </h1>


 

         <p className="fade-in-3" style={{ fontSize: 'clamp(15px,2vw,18px)', color: 'rgba(255,255,255,0.45)', maxWidth: '520px', lineHeight: 1.7, marginBottom: '40px', fontWeight: 400 }}>

           A unified platform for clinics — streamlining appointments, queues, and patient records across every role.

         </p>


 

         <div className="fade-in-4" style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', marginBottom: '60px' }}>

           <button className="btn-primary-glow" onClick={() => setView('patientLogin')}>

             Book an Appointment →

           </button>

           <button className="btn-ghost" onClick={() => setView('login')}>

             Staff Access

           </button>

         </div>


 

         {/* Feature pills */}

         <div className="fade-in-4" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '80px' }}>

           {['Queue Management', 'Walk-in Registration', 'Smart Scheduling', 'Medical History', 'Real-time Status'].map(f => (

             <span key={f} className="feature-pill">

               <span style={{ color: '#20c997', fontSize: '8px' }}>●</span>

               {f}

             </span>

           ))}

         </div>


 

         {/* ── STATS STRIP ── */}

         <div style={{

           display: 'flex', flexWrap: 'wrap',

           background: 'rgba(255,255,255,0.02)',

           border: '1px solid rgba(255,255,255,0.06)',

           borderRadius: '16px',

           padding: '24px 0',

           marginBottom: '80px'

         }}>

           {[

             { val: '4', label: 'Portals' },

             { val: '∞', label: 'Patients' },

             { val: '99.9%', label: 'Uptime' },

             { val: '< 2s', label: 'Response Time' }

           ].map((s, i) => (

             <div key={i} className="stat-item" style={{ flex: '1', minWidth: '120px', textAlign: 'center' }}>

               <div className="stat-number">{s.val}</div>

               <div className="stat-label">{s.label}</div>

             </div>

           ))}

         </div>

       </div>


 

       {/* ── PORTAL SELECTION ── */}

       <div className="content-layer" style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 clamp(20px,5vw,60px) 80px' }}>


 

         <div style={{ marginBottom: '48px' }}>

           <p style={{ fontSize: '0.75rem', fontWeight: 700, color: 'rgba(255,255,255,0.3)', letterSpacing: '2px', textTransform: 'uppercase', marginBottom: '12px' }}>Select Your Portal</p>

           <h2 style={{ fontFamily: 'Syne, sans-serif', fontSize: 'clamp(24px,4vw,40px)', fontWeight: 800, color: '#fff', letterSpacing: '-1px', margin: 0 }}>Who are you today?</h2>

         </div>


 

         <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '16px' }}>

           {[

             {

               label: 'Admin Portal',

               desc: 'Manage doctors, clinic rules, and view analytics',

               icon: '⚙️',

               accent: '#6a11cb',

               glow: 'radial-gradient(circle at 0% 0%, rgba(106,17,203,0.08) 0%, transparent 60%)',

               action: () => { setLoginRole('Admin'); setView('login'); }

             },

             {

               label: 'Patient Portal',

               desc: 'Book appointments, view history & passes',

               icon: '🩺',

               accent: '#ff7e5f',

               glow: 'radial-gradient(circle at 0% 0%, rgba(255,126,95,0.08) 0%, transparent 60%)',

               action: () => setView('patientLogin')

             },

             {

               label: 'Doctor Console',

               desc: 'Manage your queue and patient consultations',

               icon: '👨‍⚕️',

               accent: '#0beacb',

               glow: 'radial-gradient(circle at 0% 0%, rgba(11,234,203,0.08) 0%, transparent 60%)',

               action: () => setView('doctorLogin')

             },

             {

               label: 'Reception Desk',

               desc: 'Walk-ins, check-ins & live queue coordination',

               icon: '🏥',

               accent: '#f2994a',

               glow: 'radial-gradient(circle at 0% 0%, rgba(242,153,74,0.08) 0%, transparent 60%)',

               action: () => setView('receptionistLogin')

             }

           ].map((portal, i) => (

             <div

               key={i}

               className="portal-card"

               style={{ '--card-accent': portal.accent, '--card-glow': portal.glow } as any}

               onClick={portal.action}

             >

               <div className="card-icon-wrap" style={{ background: `${portal.accent}18`, color: portal.accent }}>

                 {portal.icon}

               </div>

               <div className="card-title">{portal.label}</div>

               <div className="card-desc">{portal.desc}</div>

               <div className="card-arrow" style={{ '--card-accent': portal.accent } as any}>

                 Enter Portal

                 <span style={{ fontSize: '1rem' }}>→</span>

               </div>


 

               {/* Top border accent */}

               <div style={{ position: 'absolute', top: 0, left: 0, right: 0, height: '1px', background: `linear-gradient(90deg, transparent, ${portal.accent}50, transparent)`, opacity: 0, transition: 'opacity 0.3s' }} className="card-top-line" />

             </div>

           ))}

         </div>

       </div>


 

       {/* ── DASHBOARD PREVIEW ── */}

       <div className="content-layer" style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 clamp(20px,5vw,60px) 100px' }}>


 

         <div style={{ textAlign: 'center', marginBottom: '48px' }}>

           <p style={{ fontSize: '0.75rem', fontWeight: 700, color: 'rgba(255,255,255,0.3)', letterSpacing: '2px', textTransform: 'uppercase', marginBottom: '12px' }}>Live Analytics</p>

           <h2 style={{ fontFamily: 'Syne, sans-serif', fontSize: 'clamp(24px,4vw,40px)', fontWeight: 800, color: '#fff', letterSpacing: '-1px', margin: 0 }}>Clinic Intelligence Dashboard</h2>

         </div>


 

         <div className="dashboard-preview">

           <div className="preview-bar">

             <div className="traffic-dot" style={{ background: '#ff5f57' }} />

             <div className="traffic-dot" style={{ background: '#ffbd2e' }} />

             <div className="traffic-dot" style={{ background: '#28c840' }} />

             <span style={{ marginLeft: '12px', fontSize: '0.72rem', color: 'rgba(255,255,255,0.25)', fontWeight: 500 }}>ClinicQ Analytics · Today</span>

             <div style={{ marginLeft: 'auto', display: 'flex', alignItems: 'center', gap: '6px' }}>

               <div style={{ width: 6, height: 6, borderRadius: '50%', background: '#20c997', animation: 'pulseDot 2s ease-in-out infinite' }} />

               <span style={{ fontSize: '0.7rem', color: '#20c997', fontWeight: 600 }}>LIVE</span>

             </div>

           </div>


 

           <div style={{ padding: '24px', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', gap: '12px', marginBottom: '4px' }}>

             {[

               { val: '124', label: 'Total Today', color: '#ff7e5f' },

               { val: '38', label: 'Walk-ins', color: '#6a11cb' },

               { val: '79', label: 'Completed', color: '#20c997' },

               { val: '7', label: 'No-Shows', color: '#ff4757' }

             ].map((m, i) => (

               <div key={i} className="mini-metric">

                 <div className="mini-metric-val" style={{ color: m.color }}>{m.val}</div>

                 <div className="mini-metric-label">{m.label}</div>

               </div>

             ))}

           </div>


 

           {/* Chart row */}

           <div style={{ padding: '0 24px 24px', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>

             {/* Bar chart */}

             <div style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', borderRadius: '12px', padding: '16px' }}>

               <div style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.35)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.5px', marginBottom: '16px' }}>Specialty Breakdown</div>

               <div style={{ display: 'flex', alignItems: 'flex-end', gap: '8px', height: '80px' }}>

                 {[

                   { h: '60%', c: '#ff7e5f', l: 'GEN' },

                   { h: '90%', c: '#6a11cb', l: 'CARD' },

                   { h: '40%', c: '#f2994a', l: 'ORTH' },

                   { h: '70%', c: '#20c997', l: 'PED' }

                 ].map((b, i) => (

                   <div key={i} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '4px', height: '100%', justifyContent: 'flex-end' }}>

                     <div className="chart-bar" style={{ width: '100%', height: b.h, background: `${b.c}40`, border: `1px solid ${b.c}60`, animationDelay: `${i * 0.1}s` }} />

                     <span style={{ fontSize: '0.6rem', color: 'rgba(255,255,255,0.3)', fontWeight: 600 }}>{b.l}</span>

                   </div>

                 ))}

               </div>

             </div>


 

             {/* Donut / wave chart */}

             <div style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', borderRadius: '12px', padding: '16px' }}>

               <div style={{ fontSize: '0.7rem', color: 'rgba(255,255,255,0.35)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.5px', marginBottom: '16px' }}>Visits This Week</div>

               <svg viewBox="0 0 200 80" style={{ width: '100%', height: '80px' }}>

                 <defs>

                   <linearGradient id="lineGrad" x1="0" y1="0" x2="1" y2="0">

                     <stop offset="0%" stopColor="#ff7e5f" />

                     <stop offset="100%" stopColor="#c471ed" />

                   </linearGradient>

                 </defs>

                 <path d="M0,60 Q30,20 50,40 T100,25 T150,45 T200,15" fill="none" stroke="url(#lineGrad)" strokeWidth="2.5" className="wave-line" strokeLinecap="round" />

                 <path d="M0,60 Q30,20 50,40 T100,25 T150,45 T200,15 L200,80 L0,80 Z" fill="url(#lineGrad)" fillOpacity="0.06" />

               </svg>

             </div>

           </div>

         </div>

       </div>


 

       {/* ── FOOTER ── */}

       <div className="content-layer" style={{ borderTop: '1px solid rgba(255,255,255,0.05)', padding: '32px clamp(20px,5vw,60px)' }}>

         <div style={{ maxWidth: '1280px', margin: '0 auto', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>

           <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>

             <div className="logo-mark" style={{ width: 28, height: 28, borderRadius: 8 }}>

               <FaClinicMedical style={{ color: '#fff', fontSize: '0.8rem' }} />

             </div>

             <span style={{ fontFamily: 'Syne, sans-serif', fontWeight: 700, color: 'rgba(255,255,255,0.5)', fontSize: '0.9rem' }}>ClinicQ</span>

           </div>

           <span style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.2)', fontWeight: 500 }}>

             Healthcare Management Platform · v2.0.4 · 2026

           </span>

         </div>

       </div>

     </div>

   );

 }


 

 // --- 2. AUTH VIEWS (Unchanged) ---

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

       onRegisterSuccess={() => {

         Swal.fire('Success', 'Account Created! Please login.', 'success');

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

       <AdminDashboard onToggleForm={(isOpen) => setIsAdminFormOpen(isOpen)} />

     </>

   );

 }


 

 if (view === 'patient') {

   return (

     <>

       {patientSubView === 'book' && <BookAppointment onNavigate={setPatientSubView} />}

       {patientSubView === 'my' && <MyAppointments onNavigate={setPatientSubView} onReturnHome={() => setView('home')} />}

       {patientSubView === 'history' && <PastHistory onNavigate={setPatientSubView} />}

     </>

   );

 }


 

 if (view === 'doctor') {

   return <DoctorDashboard />;

 }


 

 if (view === 'receptionist') {

   return <ReceptionistDashboard />;

 }


 

 return null;

}


 

export default App;