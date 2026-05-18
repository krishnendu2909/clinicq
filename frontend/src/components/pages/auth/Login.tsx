// import axios from 'axios';

// import React, { useState } from 'react';

// import { FaArrowLeft } from 'react-icons/fa';

// interface LoginProps {

//     onLogin: () => void;


 

// }

// const Login: React.FC<LoginProps> = ({ onLogin }) => {

//     const [email, setEmail] = useState('');

//     const [password, setPassword] = useState('');

//     const [message, setMessage] = useState('');



 

//     const handleLogin = async (e: React.FormEvent) => {


 

//         e.preventDefault();

//         try {

//             const response = await axios.post(

//                 "http://localhost:8080/clinicq/auth/login", null,

//                 {

//                     params: {

//                         email: email,

//                         password: password,

//                         role:"ADMIN"

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

//             {/* Main Login card */}

//             <div className="card border-0 rounded-5 shadow-lg"

//                 style={{ maxWidth: '420px', width: '100%', backgroundColor: '#1c1f26', border: '2px solid #00f2fe' }}>

//                 <div className="card-body p-5">

//                     {/* TOP Section - Title

//                     {!showResetPassword && (*/}

//                         <div className="text-center mb-5">

//                             <h2 className="fw-bold text-white mt-3 mb-1 text-uppercase tracking-wider">Admin Login</h2>

//                             <p className="text-info small opacity-80">Secure Access Protocol</p>

//                             {message && (<p className='text-warning text-center small'>{message}</p>)}

//                         </div>



 

//                         <form onSubmit={handleLogin}>

//                             {/* User ID section */}

//                             <div className="mb-3">

//                                 <label className="form-label text-info small fw-bold ms-1 text-uppercase">EMAIL ID</label>

//                                 <input type="email" className="form-control bg-light text-primary p-3 rounded-4 shadow-none border-secondary"

//                                     placeholder="Enter your email ID..."

//                                     value={email}

//                                     onChange={(e) => setEmail(e.target.value)} required />

//                             </div>


 

//                             {/* Password section */}

//                             <div className="mb-3">

//                                 <label className="form-label text-info small fw-bold ms-1 text-uppercase">Password</label>

//                                 <input type="password" className="form-control bg-light text-primary p-3 rounded-4 shadow-none border-secondary"

//                                     placeholder="Enter your password..."

//                                     value={password}

//                                     onChange={(e) => setPassword(e.target.value)} required />

//                             </div>



 

//                             {/* Bright contrasting button */}

//                             <button type="submit"

//                                 className="btn w-100 py-3 rounded-pill fw-bold shadow-lg mt-2"

//                                 style={{

//                                     background: 'linear-gradient(90deg,#00f2fe 0%,#4facfe 100%)',

//                                     border: 'none',

//                                     color: '#000',

//                                     fontSize: '1.1rem'

//                                 }}>UNSEAL DASHBOARD</button>


 

//                         </form>



 

//                     {/* BACK TO HOME LINK */}

//                     <div className="text-center mt-4 pt-2 border-top border-secondary">

//                         <button className="btn btn-link text-white text-decoration-none small opacity-75"

//                             onClick={() => window.location.reload()}>

//                             <FaArrowLeft style={{ marginRight: '8px', marginBottom: '2px' }} />Return to Main Entry

//                         </button>

//                     </div>



 

//                 </div>

//             </div>

//         </div>

//     );

// };

// export default Login;

import axios from 'axios';

import Swal from 'sweetalert2';

import React, { useState } from 'react';

import { FaArrowLeft } from 'react-icons/fa';

import { useNavigate } from 'react-router-dom';

interface LoginProps {

    // onLogin: () => void;

}


 

const Login: React.FC<LoginProps> = () => {

    const navigate=useNavigate();

    const [email, setEmail] = useState('');

    const [password, setPassword] = useState('');

    const [message, setMessage] = useState('');

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


 

    // --- ADD THESE NEW STATES ---

    const [otp, setOtp] = useState(['', '', '', '', '', '']);

    const [isOtpVerified, setIsOtpVerified] = useState(false);

    const [isSendingEmail, setIsSendingEmail] = useState(false);


 

    const handleOtpChange = (element: HTMLInputElement, index: number) => {

        if (isNaN(Number(element.value))) return false;

        const newOtp = [...otp];

        newOtp[index] = element.value;

        setOtp(newOtp);

        if (element.value !== "" && element.nextSibling) {

            (element.nextSibling as HTMLInputElement).focus();

        }

    };


 

    // --- LOGIC (STRICTLY UNTOUCHED) ---

    const handleForgotPassword = async (e: React.FormEvent) => {

        e.preventDefault();

        if (!emailRegex.test(email)) return setMessage("Please enter a valid email first.");


 

        // 1. Instant UI Feedback

        setIsSendingEmail(true);

        setMessage("Sending verification code...");


 

        // 2. Switch to OTP view immediately so the user isn't waiting

        setShowResetPassword(true);


 

        const formData = new URLSearchParams();

        formData.append('email', email);


 

        try {

            // 3. Send email in the background

            const response = await axios.post('http://localhost:8080/clinicq/auth/forgot-password',

                formData.toString(),

                { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }

            );


 

            setToken(response.data);

            setIsSendingEmail(false);

            setMessage("Code sent to your email!");

        } catch (error) {

            console.error('Forgot Password error:', error);

            // 4. If it fails, bring them back to login to fix the email

            setShowResetPassword(false);

            setIsSendingEmail(false);

            setMessage('Failed to send email. Please check your address.');

        }

    };

    const handleResetSubmit = async (e: React.FormEvent) => {

        e.preventDefault();

        if (newPassword !== confirmPassword) {

            return Swal.fire("Error", "Passwords do not match!", "error");

        }

        const finalOtp = otp.join('');

        try {

            await axios.post(`http://localhost:8080/clinicq/auth/reset-password?token=${finalOtp}&newPassword=${newPassword}`);

            Swal.fire({ icon: 'success', title: 'Success', text: 'Password updated successfully!', showConfirmButton: false, timer: 2000 });

            setShowResetPassword(false);

            setIsOtpVerified(false);

            setOtp(['', '', '', '', '', '']);

            setNewPassword('');

            setConfirmPassword('');

            setMessage("");

        } catch (error: any) {

            const serverMessage = error.response?.data?.errorMessage || "Failed to update password.";

            Swal.fire("Error", serverMessage, "error");

        }

    };


 

    const handleLogin = async (e: React.FormEvent) => {

        e.preventDefault();

        if (isLocked) return;

        try {

            const response = await axios.post(

                "http://localhost:8080/clinicq/auth/login", null,

                {

                    params: {

                        email: email,

                        password: password,

                        role: "ADMIN"

                    },

                });

            const token = response.data;

            localStorage.setItem("token", token);

            setAttempts(0);

            setMessage("Login successful");

            navigate('/admin');

            // onLogin();

        }

        catch (error: any) {

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

    const onEmailChange = (val: string) => {

        setEmail(val);

        setEmailValid(val === "" || emailRegex.test(val));

    };

    const onPasswordChange = (val: string) => {

        setPassword(val);

        setPasswordValid(val === "" || passwordRegex.test(val));

    };

    return (

        <div className="min-vh-100 d-flex align-items-center justify-content-center p-3"

            style={{

                // Light Theme Base: Consistent pearl-to-grey gradient

                background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)',

                fontFamily: "'Segoe UI', Roboto, sans-serif"

            }}>

            <style>

                {`

                @keyframes fadeInScale {

                    from { opacity: 0; transform: scale(0.98); }

                    to { opacity: 1; transform: scale(1); }

                }

                .admin-glass-card {

                    background: rgba(255, 255, 255, 0.7);

                    backdrop-filter: blur(30px);

                    -webkit-backdrop-filter: blur(30px);

                    border: 1px solid rgba(255, 126, 95, 0.2); /* Coral Tinted Border */

                    animation: fadeInScale 0.6s ease-out;

                    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);

                }

                .form-input-admin {

                    background: #ffffff !important;

                    border: 1px solid #dee2e6 !important;

                    color: #212529 !important;

                    transition: all 0.3s ease;

                    font-weight: 600;

                }

                .form-input-admin:focus {

                    border-color: #ff7e5f !important;

                    box-shadow: 0 0 0 0.25rem rgba(255, 126, 95, 0.1) !important;

                }

                    .otp-input-container input {

                        transition: all 0.3s ease;

                        background: #ffffff;

                    }

                        /* Bright background effect on focus */

                    .otp-input-container input:focus {

                        background: #fff5f2 !important; /* Very light coral background */

                        border-color: #ff7e5f !important;

                        box-shadow: 0 0 15px rgba(255, 126, 95, 0.4) !important; /* Glowing effect */

                        transform: translateY(-2px); /* Slight lift */

                        outline: none;

                    }


 

                        /* Animation for entering digits */

                    .otp-input-filled {

                        background: #fff !important;

                        border-color: #ff7e5f !important;

                    }


 

                `}

            </style>


 

            <div className="card admin-glass-card rounded-5 border-0" style={{ maxWidth: '420px', width: '100%' }}>

                <div className="card-body p-5">

                    {/* Header Section */}

                    <div className="text-center mb-5">

                        <div className="mx-auto mb-3 d-flex align-items-center justify-content-center shadow-sm"

                            style={{

                                width: '64px',

                                height: '64px',

                                borderRadius: '20px',

                                background: 'linear-gradient(135deg, #ff7e5f, #ff6b6b)',

                                color: '#fff'

                            }}>

                            <span style={{ fontSize: '1.8rem' }}>🔐</span>

                        </div>

                        <h2 className="fw-bold mb-1" style={{ color: '#111', fontSize: '24px', letterSpacing: '1px' }}>

                            ADMIN <span style={{ color: '#ff7e5f' }}>LOGIN</span>

                        </h2>

                        <p className="small text-uppercase fw-bold opacity-50" style={{ fontSize: '10px', letterSpacing: '1.5px' }}>

                            Secure Access Protocol

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

                            {/* Email Section */}

                            <div className="mb-3">

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f', letterSpacing: '1px' }}>Email Address</label>

                                <input

                                    type="email"

                                    className={`form-control form-input-admin p-3 rounded-4 shadow-none ${!emailValid ? 'border-danger' : ''}`}

                                    placeholder="admin@clinicq.com"

                                    value={email}

                                    onChange={(e) => onEmailChange(e.target.value)}

                                    required

                                />

                                {!emailValid && email !== "" && (

                                    <small className='text-danger ms-2' style={{ fontSize: '10px' }}>

                                        * Please enter a valid email address.

                                    </small>

                                )}

                            </div>

                            {/* Password Section */}

                            <div className="mb-2">

                                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#ff7e5f', letterSpacing: '1px' }}>Password</label>

                                <input

                                    type="password"

                                    className={`form-control form-input-admin p-3 rounded-4 shadow-none ${!passwordValid ? 'border-danger' : ''}`}

                                    placeholder="••••••••"

                                    value={password}

                                    onChange={(e) => onPasswordChange(e.target.value)}

                                    required

                                />

                                {!passwordValid && password !== "" && (

                                    <small className='text-danger ms-2' style={{ fontSize: '10px', display: 'block', lineHeight: '1.2' }}>

                                        *  Use 8+ characters with letters,numbers and special characters.

                                    </small>

                                )}

                            </div>

                            <div className='mb-2 text-end'>

                                <button type='button' className='btn btn-link text-decoration-none p-0 fw-bold shadow-none'

                                    style={{ color: '#ff7e5f', fontSize: '12px' }}

                                    onClick={handleForgotPassword}>Forgot Password?</button>

                            </div>

                            {/* Vivid Coral Button */}

                            <button

                                type="submit"

                                disabled={!emailRegex.test(email) || !passwordRegex.test(password)}

                                className="btn w-100 py-3 rounded-pill fw-bold shadow-lg mt-2 border-0 text-white"

                                style={{

                                    opacity: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 0.6 : 1,

                                    background: (!emailRegex.test(email) || !passwordRegex.test(password)) ? '#adb5bd' : 'linear-gradient(90deg, #ff7e5f 0%, #ff6b6b 100%)',

                                    fontSize: '1rem',

                                    letterSpacing: '1px',

                                    cursor: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 'not-allowed' : 'pointer'

                                }}

                            >

                                UNSEAL DASHBOARD

                            </button>

                        </form>

                    ) : (


 

                        <div className="animate-in">

                            {/* STEP 1: OTP VERIFICATION */}

                            {!isOtpVerified ? (

                                <div className="text-center">

                                    {isSendingEmail ? (


 

                                        <div className="py-4">

                                            <div className="spinner-border text-coral mb-3" style={{ color: '#ff7e5f' }}></div>

                                            <p className="fw-bold text-muted">Requesting OTP...</p>

                                        </div>

                                    ) : (


 

                                        <>


 

                                            <h5 className="fw-bold mb-3">Verification Code</h5>

                                            <p className="small text-muted mb-4">Enter the 6-digit code sent to your email.</p>


 

                                            <div className="d-flex justify-content-between mb-4 gap-2 otp-input-container">

                                                {otp.map((data, index) => (

                                                    <input

                                                        key={index}

                                                        type="text"

                                                        maxLength={1}

                                                        className={`form-control text-center fw-bold rounded-3 shadow-none ${data !== "" ? 'otp-input-filled' : ''}`}


 

                                                        style={{

                                                            width: '45px', height: '50px', fontSize: '1.2rem', borderColor: '#dee2e6',

                                                            fontWeight: '800'

                                                        }}

                                                        value={data}

                                                        onChange={e => handleOtpChange(e.target, index)}

                                                        onFocus={e => e.target.select()}

                                                        inputMode="numeric"

                                                    />

                                                ))}

                                            </div>


 

                                            {/* <button

                                                className="btn btn-coral w-100 py-3 rounded-pill fw-bold mb-2"

                                                onClick={() => { setIsOtpVerified(true); setMessage(""); }}

                                                disabled={otp.some(digit => digit === "")}

                                            >

                                                VERIFY & PROCEED

                                            </button> */}

                                            <button

                                                className="btn btn-coral w-100 py-3 rounded-pill fw-bold mb-2"

                                                type="button"

                                                disabled={otp.some(digit => digit === "") || isSendingEmail}

                                                onClick={async () => {

                                                    const finalOtp = otp.join('');

                                                    try {

                                                        // Call the brand new endpoint that doesn't consume the token

                                                        await axios.get(`http://localhost:8080/clinicq/auth/verify-token?token=${finalOtp}`);

                                                        // If the backend returns 200 OK, move them safely to the password page

                                                        setIsOtpVerified(true);

                                                        setMessage("");

                                                    } catch (error: any) {

                                                        console.error("Early OTP Check Error:", error.response);

                                                        // Your custom backend exception message or fallback text

                                                        const serverMessage = error.response?.data?.errorMessage || "Incorrect OTP. Please enter the correct OTP.";

                                                        Swal.fire({

                                                            icon: 'error',

                                                            title: 'Incorrect OTP',

                                                            text: serverMessage.includes("INVALID_USER") ? "Incorrect OTP. Please enter the correct OTP." : serverMessage,

                                                            confirmButtonColor: '#ff7e5f'

                                                        });

                                                        setIsOtpVerified(false); // Halt navigation

                                                    }

                                                }}

                                            >

                                                VERIFY & PROCEED

                                            </button>

                                        </>

                                    )}

                                    <button className="btn btn-link text-muted small text-decoration-none" onClick={() => { setShowResetPassword(false); setMessage(""); }}>Cancel</button>

                                </div>

                            ) : (

                                /* STEP 2: PASSWORD RESET */


 

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

                                            onClick={() => {

                                                setShowResetPassword(false);

                                                setAttempts(0);

                                                setMessage("");

                                            }}>Cancel</button>

                                        <button type='submit' className='btn btn-coral rounded-pill w-50 fw-bold shadow'>Update</button>

                                    </div>

                                </form>

                            )}


 

                        </div>

                    )}

                    {/* Back to Home Link */}

                    <div className="text-center mt-4 pt-3 border-top" style={{ borderColor: '#eee' }}>

                        <button className="btn btn-link text-decoration-none small fw-bold p-0"

                            style={{ color: '#6c757d', fontSize: '12px' }}

                            onClick={() => navigate('/')}>

                            <FaArrowLeft style={{ marginRight: '8px' }} /> Return to Main Entry

                        </button>

                    </div>

                </div>

            </div>

        </div>

    );

};


 

export default Login;