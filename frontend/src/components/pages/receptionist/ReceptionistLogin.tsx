// import React, { useState } from 'react';

// import axios from "axios";

// import { FaArrowLeft } from 'react-icons/fa';


 

// const ReceptionistLogin: React.FC<{ onLogin: () => void }> = ({ onLogin }) => {


 

//   const [email, setEmail] = useState("");

//   const [password, setPassword] = useState("");

//   const [message, setMessage] = useState("");

//   // const [showResetPassword, setShowResetPassword] = useState(false);

//   // const [newPassword, setNewPassword] = useState('');

//   // const [confirmPassword, setConfirmPassword] = useState('');


 

//   // const handleResetSubmit = (e: React.FormEvent) => {

//   //   e.preventDefault();

//   //   if (newPassword !== confirmPassword) {

//   //     alert("Passwords do not match!");

//   //     return;

//   //   }

//   //   setShowResetPassword(false);

//   // };


 

//   const handleLogin = async (e: React.FormEvent) => {


 

//     e.preventDefault();

//     try {

//       const response = await axios.post(

//         "http://localhost:8080/clinicq/auth/login", null,

//         {

//           params: {

//             email: email,

//             password: password,

//             role:"RECEPTIONIST"

//           },


 

//         });


 

//       const token = response.data;


 

//       localStorage.setItem("token", token);

//       setMessage("Login successful");

//       onLogin();

//     }

//     catch (error: any) {

//       console.error(error);

//       setMessage("Invalid email or password");

//     }

//   };

//   return (

//     // Fixed height container with hidden overflow to strictly prevent scrolling

//     <div className="vh-100 d-flex align-items-center justify-content-center bg-black overflow-hidden p-3">


 

//       <div className="card bg-dark border-secondary p-4 rounded-5 shadow-lg"

//         style={{ width: '400px', backgroundColor: '#0f0f0f', border: '1px solid #333' }}>


 

//           <div className="text-center mb-3">

//             <h2 className="text-white fw-bold mb-2" style={{ fontSize: '28px' }}>Receptionist Login</h2>

//             <p className="text-secondary fw-bold" style={{ fontSize: '13px', letterSpacing: '0.5px' }}>RECEPTIONIST CONSOLE ACCESS</p>

//             {message && (<p className='text-warning text-center small'>{message}</p>)}

//           </div>


 

//           <form onSubmit={handleLogin}>

//             <div className="mb-4">

//               {/* Label increased to 12px for clarity */}

//               <label className="form-label text-info fw-bold mb-1 ms-1" style={{ fontSize: '12px', letterSpacing: '1px' }}>EMAIL ID</label>

//               <input

//                 type="email"

//                 className="form-control bg-light border-secondary text-black py-2 rounded-3 shadow-none fw-bold"

//                 value={email}

//                 onChange={(e) => setEmail(e.target.value)}

//                 placeholder="staff@clinicq.com"

//                 style={{ fontSize: '16px' }} required

//               />

//             </div>


 

//             <div className="mb-4">

//               <label className="form-label text-info fw-bold mb-1 ms-1" style={{ fontSize: '12px', letterSpacing: '1px' }}>PASSWORD</label>

//               <input

//                 type="password"

//                 className="form-control bg-light border-secondary text-black py-2 rounded-3 shadow-none"

//                 value={password}

//                 onChange={(e) => setPassword(e.target.value)}

//                 placeholder="••••••••"

//                 style={{ fontSize: '16px' }} required

//               />

//             </div>

//             <button

//               type="submit"

//               className="btn btn-info w-100 py-2 rounded-pill fw-bold text-black shadow-lg transition-all"

//               style={{ letterSpacing: '1px', fontSize: '18px' }} // Larger button text

//             >

//               LOGIN

//             </button>

//           </form>


 

//         {/* BACK TO HOME LINK */}

//         <div className="text-center mt-4 pt-2 border-top border-secondary">

//           <button className="btn btn-link text-white text-decoration-none small opacity-75"

//             onClick={() => window.location.reload()}>

//             <FaArrowLeft style={{ marginRight: '8px', marginBottom: '2px' }} />Return to Main Entry

//           </button>

//         </div>

//       </div>

//     </div>

//   );

// };


 

// export default ReceptionistLogin;



 

import React, { useState } from 'react';

import Swal from 'sweetalert2';

import axios from "axios";

import { FaArrowLeft } from 'react-icons/fa';

import { useNavigate } from 'react-router-dom';


 

const ReceptionistLogin: React.FC<{ onLogin: () => void }> = () => {

  const navigate=useNavigate();

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

  // --- ADD THESE NEW STATES ---

  const [otp, setOtp] = useState(['', '', '', '', '', '']); // Array for 6 digits

  const [isOtpVerified, setIsOtpVerified] = useState(false); // Toggle between OTP and Password screen

  const [isSendingEmail, setIsSendingEmail] = useState(false);


 

  // --- ADD THIS HELPER FOR AUTO-FOCUS ---

  const handleOtpChange = (element: HTMLInputElement, index: number) => {

    if (isNaN(Number(element.value))) return false; // Only numbers

    const newOtp = [...otp];

    newOtp[index] = element.value;

    setOtp(newOtp);


 

    // Auto-focus next input

    if (element.value !== "" && element.nextSibling) {

      (element.nextSibling as HTMLInputElement).focus();

    }

  };



 

  // --- FUNCTIONALITY (STRICTLY UNTOUCHED) ---

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



 

  // const handleForgotPassword = async (e: React.FormEvent) => {

  //   e.preventDefault();

  //   setAttempts(0);

  //   setMessage("");

  //   const formData = new URLSearchParams();

  //   formData.append('email', email);

  //   try {

  //     const response = await axios.post('http://localhost:8080/clinicq/auth/forgot-password',

  //       formData.toString(),

  //       { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }

  //     );

  //     setToken(response.data);

  //     //setMessage('Token generated successfully. Please enter your new password');

  //     setShowResetPassword(true);

  //   } catch (error) {

  //     console.error('Forgot Password error:', error);

  //     setMessage('Failed to send email. Please check your email address');

  //   }

  // };

  const handleResetSubmit = async (e: React.FormEvent) => {

    e.preventDefault();

    if (newPassword !== confirmPassword) {

      return Swal.fire("Error", "Passwords do not match!", "error");

    }


 

    const finalOtp = otp.join(''); // Join ['1','2'...] into '123456'


 

    try {

      await axios.post(`http://localhost:8080/clinicq/auth/reset-password?token=${finalOtp}&newPassword=${newPassword}`);


 

      Swal.fire({ icon: 'success', title: 'Success', text: 'Password updated successfully!', showConfirmButton: false, timer: 2000 });


 

      // Reset everything back to Login

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


 

  // const handleResetSubmit = async (e: React.FormEvent) => {

  //   e.preventDefault();

  //   if (newPassword !== confirmPassword) {

  //     alert("Passwords do not match!");

  //     return;

  //   }

  //   try {

  //     const response = await axios.post(`http://localhost:8080/clinicq/auth/reset-password?token=${token}&newPassword=${newPassword}`);

  //     setMessage(response.data);

  //     setShowResetPassword(false);

  //   } catch (error) {

  //     console.error('Reset Password error: ', error);

  //     setMessage('Failed to update password.');

  //   }

  // };

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

            role: "RECEPTIONIST"

          },

        });

      const token = response.data;

      localStorage.setItem("token", token);

      setAttempts(0);

      setMessage("Login successful");

      // onLogin();

      navigate('/receptionist');

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

  return (

    <div className="min-vh-100 d-flex align-items-center justify-content-center p-3"

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

        .reception-glass-card {

            background: rgba(255, 255, 255, 0.7);

            backdrop-filter: blur(30px);

            -webkit-backdrop-filter: blur(30px);

            border: 1px solid rgba(242, 201, 76, 0.2);

            animation: fadeInScale 0.6s ease-out;

            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);

        }

        .form-input-staff {

            background: #ffffff !important;

            border: 1px solid #dee2e6 !important;

            color: #212529 !important;

            transition: all 0.3s ease;

            font-weight: 600;

        }

        .form-input-staff:focus {

            border-color: #f2c94c !important;

            box-shadow: 0 0 0 0.25rem rgba(242, 201, 76, 0.1) !important;

            outline: none;

        }

        .btn-amber {

            background: linear-gradient(90deg, #f2994a 0%, #f2c94c 100%);

            color: white; border: none; transition: transform 0.2s;

        }

        .btn-amber:hover { transform: translateY(-2px); color: white; }

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

      <div className="card reception-glass-card rounded-5 border-0" style={{ maxWidth: '400px', width: '100%' }}>

        <div className="card-body p-5">

          {/* Header Branding */}

          <div className="text-center mb-5">

            <div className="mx-auto mb-3 d-flex align-items-center justify-content-center shadow-sm"

              style={{

                width: '60px',

                height: '60px',

                borderRadius: '18px',

                background: 'linear-gradient(135deg, #f2994a, #f2c94c)',

                color: '#fff'

              }}>

              <span style={{ fontSize: '1.5rem' }}>👥</span>

            </div>

            <h2 className="fw-bold mb-1" style={{ color: '#111', fontSize: '28px' }}>

              ClinicQ <span style={{ color: '#f2994a' }}>Staff</span>

            </h2>

            <p className="small text-uppercase fw-bold opacity-50" style={{ fontSize: '10px', letterSpacing: '1.5px' }}>

              Receptionist Console Access

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

              {/* ID Section */}

              <div className="mb-3">

                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#f2994a', letterSpacing: '1px' }}>Staff ID / Email</label>

                <input

                  type="email"

                  className={`form-control form-input-staff p-3 rounded-4 shadow-none ${!emailValid ? 'border-danger' : ''}`}

                  placeholder="staff@clinicq.com"

                  value={email}

                  onChange={(e) => {

                    setEmail(e.target.value);

                    setEmailValid(e.target.value === "" || emailRegex.test(e.target.value));

                  }}

                  required

                />

                {!emailValid && email !== "" && (

                  <small className='text-danger ms-2' style={{ fontSize: '10px' }}>* Please enter a valid email.</small>

                )}

              </div>

              {/* Password Section */}

              <div className="mb-3">

                <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#f2994a', letterSpacing: '1px' }}>Password</label>

                <input

                  type="password"

                  className={`form-control form-input-staff p-3 rounded-4 shadow-none ${!passwordValid ? 'border-danger' : ''}`}

                  placeholder="••••••••"

                  value={password}

                  onChange={(e) => {

                    setPassword(e.target.value);

                    setPasswordValid(e.target.value === "" || passwordRegex.test(e.target.value));

                  }}

                  required

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

              {/* Vivid Amber Button */}

              <button

                type="submit"

                disabled={!emailRegex.test(email) || !passwordRegex.test(password)}

                className="btn btn-amber w-100 py-3 rounded-pill fw-bold text-white shadow-lg mb-2"

                style={{

                  fontSize: '1rem', letterSpacing: '0.5px',

                  opacity: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 0.6 : 1,

                  background: (!emailRegex.test(email) || !passwordRegex.test(password)) ? '#adb5bd' : '',

                  cursor: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 'not-allowed' : 'pointer'

                }}

              >

                LOG IN TO CONSOLE

              </button>

            </form>

          ) : (

            <div className="animate-in">

              {/* STEP 1: OTP VERIFICATION */}

              {!isOtpVerified ? (

                <div className="text-center">

                  {isSendingEmail ? (

                    /* SHOW THIS WHILE EMAIL IS SENDING */

                    <div className="py-4">

                      <div className="spinner-border text-coral mb-3" style={{ color: '#ff7e5f' }}></div>

                      <p className="fw-bold text-muted">Requesting OTP...</p>

                    </div>

                  ) : (

                    /* SHOW THIS ONCE EMAIL IS SENT */

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

          {/* Footer Navigation */}

          <div className="text-center mt-3 pt-3 border-top" style={{ borderColor: '#eee' }}>

            <button

              className="btn btn-link text-decoration-none small fw-bold p-0"

              style={{ color: '#6c757d', fontSize: '12px' }}

              onClick={() => navigate('/')}

            >

              <FaArrowLeft style={{ marginRight: '8px' }} /> Return to Main Entry

            </button>

          </div>

        </div>

      </div>

    </div>

  );

};

export default ReceptionistLogin;