// import React from 'react';

// interface NavbarProps{

//     activeTab:string;

//     onNavigate:(tab:'book' |'my' |'history')=>void;

// }

// const PatientNavbar:React.FC<NavbarProps>=({activeTab,onNavigate})=>{

//     return(

//         <nav className="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary shadow-sm">

//             <div className="container-fluid px-4">

//                 {/* LOGO */}

//                 <div className="navbar-brand d-flex align-items-center" style={{cursor:'pointer'}}

//                 onClick={()=>onNavigate('book')}>

//                     <div className="bg-teal rounded-circle me-2" style={{width:'30px',height:'30px',backgroundColor:'#20c997'}}></div>

//                     <span className="fw-bold text-white">ClinicQ</span>

//                 </div>


 

//                 {/* MOBILE MENU BUTTON */}

//                 <button className="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#patientNav">

//                     <span className="navbar-toggler-icon"></span>

//                 </button>


 

//                 {/* NAV LINKS */}

//                 <div className="collapse navbar-collapse" id="patientNav">

//                     <ul className="navbar-nav ms-auto gap-3">

//                         <li className="nav-item">

//                             <button className={`nav-link btn border-0 ${activeTab==='book'?'text-teal fw-bold border-bottom border-teal':'text-secondary'}`}

//                             onClick={()=>onNavigate('book')}

//                             style={{color:activeTab==='book'?'#20c997':''}}>Book Appointment</button>

//                         </li>

//                         <li className="nav-item">

//                             <button className={`nav-link btn border-0 ${activeTab==='my'?'text-teal fw-bold border-bottom border-teal':'text-secondary'}`}

//                             onClick={()=>onNavigate('my')}

//                             style={{color:activeTab==='my'?'#20c997':''}}>My Appointments</button>

//                         </li>

//                     </ul>

//                 </div>

//             </div>

//         </nav>

//     );

// };

// export default PatientNavbar;

import React from 'react';



 




 

interface NavbarProps {


 

    activeTab: string;


 

    onNavigate: (tab: 'book' | 'my' | 'history') => void;


 

}



 




 

const PatientNavbar: React.FC<NavbarProps> = ({ activeTab, onNavigate }) => {


 

    return (


 

        <nav className="navbar navbar-expand-lg sticky-top shadow-sm"


 

            style={{


 

                background: 'rgba(255, 255, 255, 0.8)',


 

                backdropFilter: 'blur(20px)',


 

                WebkitBackdropFilter: 'blur(20px)',


 

                borderBottom: '1px solid rgba(0, 0, 0, 0.05)'


 

            }}>


 

            <div className="container px-4">


 

                {/* LOGO - Matching Landing Page Branding */}


 

                <div className="navbar-brand d-flex align-items-center"


 

                    style={{ cursor: 'pointer' }}


 

                    onClick={() => onNavigate('book')}>


 

                    <div className="rounded-circle me-2 d-flex align-items-center justify-content-center shadow-sm"


 

                        style={{


 

                            width: '35px',


 

                            height: '35px',


 

                            background: '#ff7e5f', // Coral Theme


 

                            color: '#fff',


 

                            fontSize: '1rem'


 

                        }}>


 

                        🏠


 

                    </div>


 

                    <span className="fw-bold" style={{ color: '#111', letterSpacing: '0.5px' }}>ClinicQ</span>


 

                </div>



 




 

                {/* MOBILE MENU BUTTON */}


 

                <button className="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#patientNav">


 

                    <span className="navbar-toggler-icon"></span>


 

                </button>



 




 

                {/* NAV LINKS */}


 

                <div className="collapse navbar-collapse" id="patientNav">


 

                    <ul className="navbar-nav ms-auto gap-2 align-items-center">


 

                        <li className="nav-item">


 

                            <button


 

                                className="nav-link btn border-0 px-3 py-2"


 

                                onClick={() => onNavigate('book')}


 

                                style={{


 

                                    borderRadius: '10px',


 

                                    transition: 'all 0.3s ease',


 

                                    color: activeTab === 'book' ? '#ff7e5f' : '#6c757d',


 

                                    fontWeight: activeTab === 'book' ? '700' : '500',


 

                                    background: activeTab === 'book' ? 'rgba(255, 126, 95, 0.08)' : 'transparent'


 

                                }}>


 

                                📅 Book Appointment


 

                            </button>


 

                        </li>


 

                        <li className="nav-item">


 

                            <button


 

                                className="nav-link btn border-0 px-3 py-2"


 

                                onClick={() => onNavigate('my')}


 

                                style={{


 

                                    borderRadius: '10px',


 

                                    transition: 'all 0.3s ease',


 

                                    color: activeTab === 'my' ? '#ff7e5f' : '#6c757d',


 

                                    fontWeight: activeTab === 'my' ? '700' : '500',


 

                                    background: activeTab === 'my' ? 'rgba(255, 126, 95, 0.08)' : 'transparent'


 

                                }}>


 

                                📋 My Appointments


 

                            </button>


 

                        </li>


 

                       


 

                        {/* Profile Indicator */}


 

                        <li className="nav-item ms-lg-3">


 

                            <div className="rounded-circle border d-flex align-items-center justify-content-center"


 

                                style={{


 

                                    width: '38px',


 

                                    height: '38px',


 

                                    background: '#f8f9fa',


 

                                    cursor: 'pointer',


 

                                    transition: 'all 0.2s'


 

                                }}


 

                                onMouseOver={(e) => e.currentTarget.style.background = '#eee'}


 

                                onMouseOut={(e) => e.currentTarget.style.background = '#f8f9fa'}


 

                            >


 

                                <span style={{ fontSize: '1.1rem' }}>👤</span>


 

                            </div>


 

                        </li>


 

                    </ul>


 

                </div>


 

            </div>


 

        </nav>


 

    );


 

};



 




 

export default PatientNavbar;
