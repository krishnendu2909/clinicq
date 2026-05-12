// import React from 'react';

// import { FaRightFromBracket } from 'react-icons/fa6';


 

// interface SidebarProps {

//     activeTab: string;

//     setActiveTab: (tab: 'scheduling' | 'rules' | 'analytics') => void;

// }

// const Sidebar: React.FC<SidebarProps> = ({ activeTab, setActiveTab }) => {

//     return (

//         <div className="bg-dark border-end border-info p-3" style={{ width: '260px', minHeight: '100vh' }}>


 

//             {/* 1. Clinic Logo (Top of the sidebar) */}

//             <div className="d-flex align-items-center mb-5 ps-2">

//                 <div className="bg-info rounded-circle me-2 shadow-sm" style={{ width: '35px', height: '35px', border: '2px solid white' }}></div>

//                 <h3 className="text-info m-0 fw-bold" style={{ letterSpacing: '1px' }}>ClinicQ</h3>

//             </div>


 

//             {/* 2. NAVIGATION LINKS */}

//             <ul className="nav flex-column gap-3">

//                 <li className="nav-item">

//                     <button className={`btn w-100 text-start d-flex align-items-center gap-2 ${activeTab === 'scheduling' ? 'btn-info text-black fw-bold' : 'text-info border-0'}`}

//                         onClick={() => setActiveTab('scheduling')}>

//                         <span></span>Doctor Scheduling

//                     </button>

//                 </li>

//                 <li className="nav-item">

//                     <button className={`btn w-100 text-start d-flex align-items-center gap-2 ${activeTab === 'rules' ? 'btn-info text-black fw-bold' : 'text-info border-0'}`}

//                         onClick={() => setActiveTab('rules')}>

//                         <span></span>Clinic Rules

//                     </button>

//                 </li>

//                 <li className="nav-item">

//                     <button className={`btn  w-100 text-start d-flex align-items-center gap-2 ${activeTab === 'analytics' ? 'btn-info text-black fw-bold' : 'text-info border-0'}`}

//                         onClick={() => setActiveTab('analytics')}>

//                         <span></span>Analytics

//                     </button>

//                 </li>

//                 <li className='nav-item'>

//                     <div className="border-top border-secondary mt-auto pt-2">

//                         <button onClick={() => window.location.reload()} className="btn btn-outline-danger w-100 text-start py-2 px-3 rounded-3 border-0 btn-sm d-flex align-items-center">

//                             <span></span><span className="fw-bold" style={{ fontSize: '20px' }}>Log Out</span>

//                         </button>

//                     </div>

//                 </li>


 

//             </ul>


 

//             {/* 3. BOTTOM DECORATION

//             <div className="mt-auto pt-5 ps-2">

//                 <small className="text-secondary">Admin Mode v1.0</small>

//             </div>

//             */}

//         </div>

//     );

// };


 

// export default Sidebar;

import React from 'react';


 

import { FaRightFromBracket } from 'react-icons/fa6';



 




 

interface SidebarProps {


 

    activeTab: string;


 

    setActiveTab: (tab: 'scheduling' | 'rules' | 'analytics') => void;


 

}



 




 

const Sidebar: React.FC<SidebarProps> = ({ activeTab, setActiveTab }) => {


 

    return (


 

        <div className="d-flex flex-column p-3 border-end shadow-sm"


 

            style={{


 

                width: '260px',


 

                minHeight: '100vh',


 

                background: '#ffffff', // Clean white background for Light Theme


 

                borderColor: '#f1f3f5 !important'


 

            }}>



 




 

            {/* 1. Clinic Logo (Matches Landing Page Coral Theme) */}


 

            <div className="d-flex align-items-center mb-5 ps-2 pt-2">


 

                <div className="rounded-circle me-3 d-flex align-items-center justify-content-center fw-bold shadow-sm"


 

                    style={{


 

                        width: '42px',


 

                        height: '42px',


 

                        background: 'linear-gradient(135deg, #ff7e5f, #ff6b6b)',


 

                        color: 'white',


 

                        fontSize: '1.2rem'


 

                    }}>


 

                    🏠


 

                </div>


 

                <h3 className="m-0 fw-bold" style={{ letterSpacing: '0.5px', color: '#111' }}>ClinicQ</h3>


 

            </div>



 




 

            {/* 2. NAVIGATION LINKS */}


 

            <ul className="nav flex-column gap-2">


 

                {[


 

                    { id: 'scheduling', label: 'Doctor Scheduling', icon: '📅' },


 

                    { id: 'rules', label: 'Clinic Rules', icon: '⚙️' },


 

                    { id: 'analytics', label: 'Analytics', icon: '📊' }


 

                ].map((item) => (


 

                    <li className="nav-item" key={item.id}>


 

                        <button


 

                            className="btn w-100 text-start d-flex align-items-center gap-3 px-3 py-2 border-0"


 

                            style={{


 

                                borderRadius: '14px',


 

                                transition: 'all 0.2s ease',


 

                                // High contrast active state (Coral-tinted background)


 

                                background: activeTab === item.id ? 'rgba(255, 126, 95, 0.1)' : 'transparent',


 

                                color: activeTab === item.id ? '#ff7e5f' : '#6c757d',


 

                                fontWeight: activeTab === item.id ? '700' : '600',


 

                            }}


 

                            onClick={() => setActiveTab(item.id as any)}


 

                        >


 

                            <span style={{ fontSize: '1.2rem' }}>{item.icon}</span>


 

                            <span style={{ fontSize: '0.9rem' }}>{item.label}</span>


 

                        </button>


 

                    </li>


 

                ))}



 




 

                {/* Log Out Button Section */}


 

                <li className="nav-item mt-4 pt-4 border-top" style={{ borderColor: '#f8f9fa !important' }}>


 

                    <button


 

                        onClick={() => window.location.reload()}


 

                        className="btn w-100 text-start d-flex align-items-center gap-3 px-3 py-2 border-0 text-danger opacity-75"


 

                        style={{ borderRadius: '14px', transition: 'all 0.2s ease',color: '#dc3545 !important'}}


 

                        onMouseOver={(e) => e.currentTarget.style.background = 'rgba(220, 53, 69, 0.05)'}


 

                        onMouseOut={(e) => e.currentTarget.style.background = 'transparent'}


 

                    >


 

                        <FaRightFromBracket style={{ fontSize: '1.1rem' }} />


 

                        <span className="fw-bold" style={{ fontSize: '0.9rem' }}>Log Out</span>


 

                    </button>


 

                </li>


 

            </ul>



 




 

            {/* 3. BOTTOM DECORATION */}


 

            <div className="mt-auto ps-1 pb-2">


 

                <div className="p-3 rounded-4" style={{ background: '#f8f9fa' }}>


 

                    <small className="text-muted d-block fw-bold opacity-75" style={{ fontSize: '10px', textTransform: 'uppercase', letterSpacing: '1px' }}>


 

                        Admin Control


 

                    </small>


 

                    <small style={{ fontSize: '11px', color: '#adb5bd', fontWeight: '500' }}>


 

                        v2.0.4 • 2026 Stable


 

                    </small>


 

                </div>


 

            </div>


 

        </div>


 

    );


 

};



 




 

export default Sidebar;