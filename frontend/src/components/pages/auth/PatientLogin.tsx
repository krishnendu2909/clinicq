// import React, { useState } from 'react';

// import axios from "axios";

// import { FaArrowLeft } from 'react-icons/fa';

// interface Props {

//     onLogin: () => void;

//     onGoToRegister: () => void;

//     onReturnHome: () => void;

// }


 

// const PatientLogin: React.FC<Props> = ({ onLogin, onGoToRegister, onReturnHome }) => {


 

//     const [email, setEmail] = useState("");

//     const [password, setPassword] = useState("");

//     const [message, setMessage] = useState("");

//     const [showResetPassword, setShowResetPassword] = useState(false);

//     const [newPassword, setNewPassword] = useState('');

//     const [confirmPassword, setConfirmPassword] = useState('');

//     const [token, setToken] = useState('');


 

//     const handleForgotPassword = async (e: React.FormEvent) => {

//         e.preventDefault();

//         const formData = new URLSearchParams();

//         formData.append('email', email);

//         try {

//             const response = await axios.post('http://localhost:8080/clinicq/auth/forgot-password',

//                 formData.toString(),

//                 {

//                     headers:{

//                         'Content-Type':'application/x-www-form-urlencoded',

//                     }

//                 }

//             );

//             setToken(response.data);

//             setMessage('Token generated successfully.Please enter your new password');

//             setShowResetPassword(true);


 

//         }catch(error){

//             console.error('Forgot Password error:',error);

//             setMessage('Failed to send email. Please check your email address');

//         }

//     };



 

//     const handleResetSubmit = async (e: React.FormEvent) => {

//         e.preventDefault();

//         if (newPassword !== confirmPassword) {

//             alert("Passwords do not match!");

//             return;

//         }

//         try{

//             const response=await axios.post(`http://localhost:8080/clinicq/auth/reset-password?token=${token}&newPassword=${newPassword}`);

//             setMessage(response.data);

//             setShowResetPassword(false);

//         }catch(error){

//             console.error('Reset Password error: ',error);

//             setMessage('Failed to update password.');

//         }


 

//     };


 

//     const handleLogin = async (e: React.FormEvent) => {

//         e.preventDefault();

//         try {

//             const response = await axios.post(

//                 'http://localhost:8080/clinicq/auth/login', null,

//                 {

//                     params: {

//                         email: email,

//                         password: password,

//                         role:"PATIENT"

//                     },


 

//                 });


 

//             const token = response.data;


 

//             localStorage.setItem("token", token);

//             setMessage("Login successful");

//             onLogin();

//         }

//         catch (error: any) {

//             console.error(error);

//             setMessage("Invalid email or password");

//         }

//     };

//     return (

//         <div className="bg-black min-vh-100 d-flex align-items-center justify-content-center p-3">

//             <div className="card border-0 rounded-5 shadow-lg" style={{ maxWidth: '420px', width: '100%', backgroundColor: '#1a1625', border: '2px solid #a55eea' }}>

//                 <div className="card-body p-5">

//                     {!showResetPassword && (

//                         <div className="text-center mb-5">

//                             <h2 className="fw-bold text-white mt-1 mb-1">{showResetPassword?'Reset Password':'PATIENT LOGIN'}</h2>

//                             <p className="text-purple small opacity-80" style={{ color: '#a55eea' }}>Access your health dashboard</p>

//                             {message && (<p className='text-warning text-center small'>{message}</p>)}

//                         </div>

//                     )}

//                     {!showResetPassword ? (

//                         <form onSubmit={handleLogin}>

//                             <div className="mb-3">

//                                 <label className="form-label small fw-bold" style={{ color: '#a55eea' }}>EMAIL ID</label>

//                                 <input type="email"

//                                     className="form-control bg-light text-purple p-3 rounded-4 border-secondary shadow-none"

//                                     value={email}

//                                     onChange={(e) => setEmail(e.target.value)}

//                                     placeholder="your@gmail.com"

//                                     required />

//                             </div>

//                             <div className="mb-2">

//                                 <label className="form-label small fw-bold" style={{ color: '#a55eea' }}>PASSWORD</label>

//                                 <input type="password"

//                                     className="form-control bg-light text-purple p-3 rounded-4 border-secondary shadow-none"

//                                     value={password}

//                                     onChange={(e) => setPassword(e.target.value)}

//                                     placeholder="••••••••"

//                                     required />

//                             </div>

//                             <div className='mb-2 text-end'>

//                                 <button

//                                     type='button'

//                                     className='btn btn-link text-decoration-none p-0'

//                                     onClick={handleForgotPassword}>Forgot Password?</button>

//                             </div>

//                             <button type="submit" className="btn w-100 py-3 rounded-pill fw-bold text-white shadow-lg mt-2"

//                                 style={{ background: 'linear-gradient(90deg,#a55eea 0%,#4b7bec 100%)' }}>LOGIN</button>


 

//                             <div className="text-center mt-3">

//                                 <p className="small text-light mb-1">New to ClinicQ?</p>

//                                 <button className="btn btn-link fw-bold text-decoration-none p-0" style={{ color: '#a55eea' }}

//                                     onClick={onGoToRegister}>Create an Account</button>

//                             </div>


 

//                         </form>




 

//                     ) : (

//                         <form onSubmit={handleResetSubmit}>

//                             <h5 className='text-primary mb-4 text-center fw-bold'>RESET PASSWORD</h5>

//                             <div className='mb-3'>

//                                 <label className='form-label text-primary fw-bold'>NEW PASSWORD</label>

//                                 <input

//                                     type='password'

//                                     className='form-control bg-light border-secondary'

//                                     value={newPassword}

//                                     onChange={(e) => setNewPassword(e.target.value)}

//                                     placeholder='Enter new password...' required />

//                             </div>

//                             <div className='mb-4'>

//                                 <label className='form-label text-primary fw-bold'>CONFIRM PASSWORD</label>

//                                 <input

//                                     type='password'

//                                     className='form-control bg-light border-secondary'

//                                     value={confirmPassword}

//                                     onChange={(e) => setConfirmPassword(e.target.value)}

//                                     placeholder='Confirm new password...' required />

//                             </div>

//                             <div className='d-flex gap-2'>

//                                 <button

//                                     type="button"

//                                     className='btn btn-secondary w-50'

//                                     onClick={() => setShowResetPassword(false)}>Back to Login</button>

//                                 <button type='submit' className='btn btn-primary w-50'>Update Password</button>

//                             </div>

//                         </form>

//                     )}


 

//                     <div className="text-center mt-4 pt-2 border-top border-secondary">

//                         <button className="btn btn-link text-white text-decoration-none small opacity-75" onClick={onReturnHome}>

//                             <FaArrowLeft style={{ marginRight: '8px', marginBottom: '2px' }} />Return to Main Entry

//                         </button>

//                     </div>

//                 </div>

//             </div>

//         </div>

//     );

// };

// export default PatientLogin;

import React, { useState } from 'react';


 

import axios from "axios";


 

import { FaArrowLeft } from 'react-icons/fa';





 

interface Props {


 

    onLogin: () => void;


 

    onGoToRegister: () => void;


 

    onReturnHome: () => void;


 

}





 

const PatientLogin: React.FC<Props> = ({ onLogin, onGoToRegister, onReturnHome }) => {


 

    const [email, setEmail] = useState("");


 

    const [password, setPassword] = useState("");


 

    const [message, setMessage] = useState("");


 

    const [showResetPassword, setShowResetPassword] = useState(false);


 

    const [newPassword, setNewPassword] = useState('');


 

    const [confirmPassword, setConfirmPassword] = useState('');


 

    const [token, setToken] = useState('');


 

    const [attempts, setAttempts] = useState(0);

    const [isLocked, setIsLocked] = useState(false);

    const [timeLeft, setTimeLeft] = useState(0);

    const [emailValid, setEmailValid] = useState(true);

    const [passwordValid, setPasswordValid] = useState(true);


 

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;


 

    // --- FUNCTIONALITY (STRICTLY UNTOUCHED) ---


 

    const handleForgotPassword = async (e: React.FormEvent) => {


 

        e.preventDefault();

        setAttempts(0);

        setMessage("");


 

        const formData = new URLSearchParams();


 

        formData.append('email', email);


 

        try {


 

            const response = await axios.post('http://localhost:8080/clinicq/auth/forgot-password',


 

                formData.toString(),


 

                { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }


 

            );


 

            setToken(response.data);


 

            //setMessage('Token generated successfully. Please enter your new password');


 

            setShowResetPassword(true);


 

        } catch (error) {


 

            console.error('Forgot Password error:', error);


 

            setMessage('Failed to send email. Please check your email address');


 

        }


 

    };


 

    const onEmailChange = (val: string) => {

        setEmail(val);

        setEmailValid(val === "" || emailRegex.test(val));

    };

    const onPasswordChange = (val: string) => {

        setPassword(val);

        setPasswordValid(val === "" || passwordRegex.test(val));

    };


 

    const handleResetSubmit = async (e: React.FormEvent) => {


 

        e.preventDefault();


 

        if (newPassword !== confirmPassword) {


 

            alert("Passwords do not match!");


 

            return;


 

        }


 

        try {


 

            const response = await axios.post(`http://localhost:8080/clinicq/auth/reset-password?token=${token}&newPassword=${newPassword}`);


 

            setMessage(response.data);


 

            setShowResetPassword(false);


 

        } catch (error) {


 

            console.error('Reset Password error: ', error);


 

            setMessage('Failed to update password.');


 

        }


 

    };





 

    const handleLogin = async (e: React.FormEvent) => {


 

        e.preventDefault();

        if (isLocked) return;


 

        try {


 

            const response = await axios.post('http://localhost:8080/clinicq/auth/login', null, {


 

                params: { email: email, password: password, role: "PATIENT" },


 

            });


 

            const tokenResponse = response.data;


 

            localStorage.setItem("token", tokenResponse);

            setAttempts(0);


 

            setMessage("Login successful");


 

            onLogin();


 

        } catch (error: any) {

            const newAttempts = attempts + 1;

            setAttempts(newAttempts);

            if (newAttempts >= 3) {

                setIsLocked(true);

                setTimeLeft(60);

                setMessage("Account Locked due to multiple failed attempts. Try after a minute.");

                const timer = setInterval(() => {

                    setTimeLeft((prev) => {

                        if (prev <= 1) {

                            clearInterval(timer);

                            setIsLocked(false);

                            setAttempts(0);

                            setMessage("");

                            return 0;

                        }

                        return prev - 1;

                    })


 

                }, 1000);

            } else {

                console.error(error);


 

                setMessage(`Invalid credentials. ${3 - newAttempts} attempts remaining.`);


 

            }


 

        }


 

    };





 

    return (


 

        <div className="min-vh-100 d-flex align-items-center justify-content-center p-1"


 

            style={{


 

                background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)',


 

                fontFamily: "'Segoe UI', Roboto, sans-serif"


 

            }}>





 

            <style>


 

                {`


 

                @keyframes fadeInScale {


 

                    from { opacity: 0; transform: scale(0.98); }


 

                    to { opacity: 1; transform: scale(1); }


 

                }


 

                .patient-glass-card {


 

                    background: rgba(255, 255, 255, 0.7);


 

                    backdrop-filter: blur(30px);


 

                    -webkit-backdrop-filter: blur(30px);


 

                    border: 1px solid rgba(255, 126, 95, 0.2);


 

                    animation: fadeInScale 0.6s ease-out;


 

                    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);


 

                }


 

                .form-input-patient {


 

                    background: #ffffff !important;


 

                    border: 1px solid #dee2e6 !important;


 

                    color: #212529 !important;


 

                    transition: all 0.3s ease;


 

                    font-weight: 600;


 

                }


 

                .form-input-patient:focus {


 

                    border-color: #ff7e5f !important;


 

                    box-shadow: 0 0 0 0.25rem rgba(255, 126, 95, 0.1) !important;


 

                }


 

                .btn-coral {


 

                    background: linear-gradient(90deg, #ff7e5f 0%, #ff6b6b 100%);


 

                    color: white; border: none; transition: transform 0.2s;


 

                }


 

                .btn-coral:hover { transform: translateY(-2px); color: white; }


 

                `}


 

            </style>





 

            <div className="card patient-glass-card rounded-5 border-0" style={{ maxWidth: '420px', width: '100%' }}>


 

                <div className="card-body p-5">




 

                    {/* Header Section */}


 

                    <div className="text-center mb-1">


 

                        <div className="mx-auto mb-3 d-flex align-items-center justify-content-center shadow-sm"


 

                            style={{


 

                                width: '64px', height: '64px', borderRadius: '20px',


 

                                background: 'linear-gradient(135deg, #ff7e5f, #ff6b6b)', color: '#fff'


 

                            }}>


 

                            <span style={{ fontSize: '1.8rem' }}>👤</span>


 

                        </div>


 

                        <h2 className="fw-bold mb-1" style={{ color: '#111', fontSize: '24px' }}>


 

                            {showResetPassword ? 'Reset Password' : 'PATIENT LOGIN'}


 

                        </h2>


 

                        <p className="small text-uppercase fw-bold opacity-50" style={{ fontSize: '10px', letterSpacing: '1.5px', color: '#ff7e5f' }}>


 

                            {showResetPassword ? 'Security Protocol' : 'Access your health dashboard'}


 

                        </p>


 

                        {message && (


 

                            <div className={`mt-3 p-2 rounded-3 small fw-bold d-flex align-items-center justigy-content-center gap-2 border ${message.includes('successful') ? 'bg-success-subtle text-success border-success-subtle' : 'bg-danger-subtle text-danger border-danger-subtle'}`}>

                                {isLocked && <span className='spinner-border spinner-border-sm me-1'></span>}

                                <span>{message} {isLocked && timeLeft > 0 && `(${timeLeft}s)`}</span>


 

                            </div>


 

                        )}


 

                    </div>





 

                    {!showResetPassword ? (


 

                        <form onSubmit={handleLogin}>


 

                            <div className="mb-2">


 

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f', letterSpacing: '1px' }}>Email ID</label>


 

                                <input


 

                                    type="email"


 

                                    className={`form-control form-input-patient p-3 rounded-4 shadow-none ${!emailValid ? 'border-danger' : ''}`}


 

                                    placeholder="your@gmail.com"


 

                                    value={email}


 

                                    onChange={(e) => onEmailChange(e.target.value)} required


 

                                />

                                {!emailValid && email !== "" && (

                                    <small className='text-danger ms-2' style={{ fontSize: '10px' }}>

                                        * Please enter a valid email address.

                                    </small>

                                )}


 

                            </div>





 

                            <div className="mb-2">


 

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f', letterSpacing: '1px' }}>Password</label>


 

                                <input


 

                                    type="password"


 

                                    className={`form-control form-input-patient p-3 rounded-4 shadow-none ${!passwordValid ? 'border-danger' : ''}`}


 

                                    placeholder="••••••••"


 

                                    value={password}


 

                                    onChange={(e) => onPasswordChange(e.target.value)} required


 

                                />

                                {!passwordValid && password !== "" && (

                                    <small className='text-danger ms-2' style={{ fontSize: '10px', display: 'block', lineHeight: '1.2' }}>

                                        * Use 8+ characters with letters,numbers and special characters.

                                    </small>

                                )}


 

                            </div>





 

                            <div className='mb-3 text-end'>


 

                                <button type='button' className='btn btn-link text-decoration-none p-0 fw-bold shadow-none'


 

                                    style={{ color: '#ff7e5f', fontSize: '12px' }}


 

                                    onClick={handleForgotPassword}>Forgot Password?</button>


 

                            </div>





 

                            <button type="submit"

                                disabled={!emailRegex.test(email) || !passwordRegex.test(password)}

                                className="btn btn-coral w-100 py-3 rounded-pill fw-bold shadow-lg mb-3"

                                style={{

                                    opacity: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 0.6 : 1,

                                    background: (!emailRegex.test(email) || !passwordRegex.test(password)) ? '#adb5bd' : '',

                                    cursor: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 'not-allowed' : 'pointer'


 

                                }}

                            >


 

                                SIGN IN


 

                            </button>





 

                            <div className="text-center">


 

                                <p className="small text-muted mb-1">New to ClinicQ?</p>


 

                                <button type="button" className="btn btn-link fw-bold text-decoration-none p-0"


 

                                    style={{ color: '#ff7e5f' }}


 

                                    onClick={onGoToRegister}>Create an Account</button>


 

                            </div>


 

                        </form>


 

                    ) : (


 

                        <form onSubmit={handleResetSubmit}>


 

                            <div className='mb-3'>


 

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f' }}>New Password</label>


 

                                <input


 

                                    type='password'


 

                                    className='form-control form-input-patient p-3 rounded-4 shadow-none'


 

                                    value={newPassword}


 

                                    onChange={(e) => setNewPassword(e.target.value)}


 

                                    placeholder='Enter new password...' required />


 

                            </div>


 

                            <div className='mb-4'>


 

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f' }}>Confirm Password</label>


 

                                <input


 

                                    type='password'


 

                                    className='form-control form-input-patient p-3 rounded-4 shadow-none'


 

                                    value={confirmPassword}


 

                                    onChange={(e) => setConfirmPassword(e.target.value)}


 

                                    placeholder='Confirm new password...' required />


 

                            </div>


 

                            <div className='d-flex gap-2'>


 

                                <button type="button" className='btn btn-light rounded-pill w-50 fw-bold border'


 

                                onClick={() => {setShowResetPassword(false);

                                setAttempts(0);

                                setMessage("");

                                }}>Cancel</button>


 

                            <button type='submit' className='btn btn-coral rounded-pill w-50 fw-bold shadow'>Update</button>


 

                        </div>


 

                        </form>


 

                    )}





 

                {/* Back to Home */}


 

                <div className="text-center mt-4 pt-3 border-top" style={{ borderColor: '#eee' }}>


 

                    <button className="btn btn-link text-decoration-none small fw-bold p-0"


 

                        style={{ color: '#6c757d', fontSize: '12px' }}


 

                        onClick={onReturnHome}>


 

                        <FaArrowLeft style={{ marginRight: '8px' }} /> Return to Main Entry


 

                    </button>


 

                </div>


 

            </div>


 

        </div>


 

        </div >


 

    );


 

};





 

export default PatientLogin;