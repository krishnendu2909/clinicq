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


 

import axios from "axios";


 

import { FaArrowLeft } from 'react-icons/fa';





 

const ReceptionistLogin: React.FC<{ onLogin: () => void }> = ({ onLogin }) => {


 

  const [email, setEmail] = useState("");


 

  const [password, setPassword] = useState("");


 

  const [message, setMessage] = useState("");



 

  const [attempts, setAttempts] = useState(0);

  const [isLocked, setIsLocked] = useState(false);

  const [timeLeft, setTimeLeft] = useState(0);


 

  const [emailValid, setEmailValid] = useState(true);

  const [passwordValid, setPasswordValid] = useState(true);


 

  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;



  // --- FUNCTIONALITY (STRICTLY UNTOUCHED) ---


 

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


 

      onLogin();


 

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




 

          <form onSubmit={handleLogin}>


 

            {/* ID Section */}


 

            <div className="mb-3">


 

              <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#f2994a', letterSpacing: '1px' }}>Staff ID / Email</label>


 

              <input


 

                type="email"


 

                className={`form-control form-input-staff p-3 rounded-4 shadow-none ${!emailValid?'border-danger':''}`}


 

                placeholder="staff@clinicq.com"


 

                value={email}


 

                onChange={(e) => {setEmail(e.target.value);

                  setEmailValid(e.target.value==="" || emailRegex.test(e.target.value));

                }}


 

                required


 

              />

              {!emailValid && email!=="" && (

                <small className='text-danger ms-2' style={{fontSize:'10px'}}>* Please enter a valid email.</small>

              )}


 

            </div>




 

            {/* Password Section */}


 

            <div className="mb-4">


 

              <label className="fw-bold mb-1 ms-1 text-uppercase" style={{ fontSize: '11px', color: '#f2994a', letterSpacing: '1px' }}>Password</label>


 

              <input


 

                type="password"


 

                className={`form-control form-input-staff p-3 rounded-4 shadow-none ${!passwordValid?'border-danger':''}`}


 

                placeholder="••••••••"


 

                value={password}


 

                onChange={(e) => {setPassword(e.target.value);

                  setPasswordValid(e.target.value==="" || passwordRegex.test(e.target.value));

                }}


 

                required


 

              />

              {!passwordValid && password !== "" && (

                <small className='text-danger ms-2' style={{fontSize:'10px',display:'block',lineHeight:'1.2'}}>

                  * Use 8+ characters with letters,numbers and special characters.

                </small>

              )}


 

            </div>




 

            {/* Vivid Amber Button */}


 

            <button


 

              type="submit"

              disabled={!emailRegex.test(email) || !passwordRegex.test(password)}

              className="btn btn-amber w-100 py-3 rounded-pill fw-bold text-white shadow-lg mb-2"


 

              style={{ fontSize: '1rem', letterSpacing: '0.5px',

                 opacity: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 0.6 : 1,

                  background: (!emailRegex.test(email) || !passwordRegex.test(password)) ? '#adb5bd' : '',

                  cursor: (!emailRegex.test(email) || !passwordRegex.test(password)) ? 'not-allowed' : 'pointer'


 

               }}


 

            >


 

              LOG IN TO CONSOLE


 

            </button>


 

          </form>





 

          {/* Footer Navigation */}


 

          <div className="text-center mt-4 pt-3 border-top" style={{ borderColor: '#eee' }}>


 

            <button


 

              className="btn btn-link text-decoration-none small fw-bold p-0"


 

              style={{ color: '#6c757d', fontSize: '12px' }}


 

              onClick={() => window.location.reload()}


 

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