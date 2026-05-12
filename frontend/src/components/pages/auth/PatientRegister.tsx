// import React, { useState } from "react";

// import axios from "axios";


 

// interface Props {

//   onRegisterSuccess: () => void;

//   onReturnToLogin: () => void;

// }


 

// const PatientRegister: React.FC<Props> = ({

//   onRegisterSuccess,

//   onReturnToLogin,

// }) => {


 

//   const [formData, setFormData] = useState({

//     name: "",

//     email: "",

//     dateOfBirth: "",

//     gender: "MALE",

//     phone: "",

//     password: "",

//   });


 

//   const [error, setError] = useState("");

//   const [showPassword, setShowPassword] = useState(false);

//   const handleChange = (e: any) => {

//     setFormData({ ...formData, [e.target.name]: e.target.value });

//   };


 

//   const handleSubmit = async (e: any) => {

//     e.preventDefault();


 

//     try {

//       const response = await axios.post(

//         "http://localhost:8080/clinicq/auth/signup",

//         {

//           name: formData.name,

//           phone: formData.phone,

//           dateOfBirth: formData.dateOfBirth,

//           gender: formData.gender,

//           user: {

//             email: formData.email,

//             password: formData.password,

//           },

//         }

//       );


 

//       console.log(response.data);

//       onRegisterSuccess();


 

//     } catch (err: any) {

//       console.error(err);

//       setError(err.response?.data || "Registration failed");

//     }

//   };


 

//   return (

//     <div className="bg-black min-vh-100 d-flex align-items-center justify-content-center p-3">

//       <div

//         className="card border-0 rounded-5 shadow-lg"

//         style={{

//           maxWidth: "500px",

//           width: "100%",

//           backgroundColor: "#161b22",

//           border: "2px solid #20c997",

//         }}

//       >

//         <div className="card-body px-4 py-2">

//           <h2 className="fw-bold text-white text-center mb-3">

//             JOIN CLINICQ

//           </h2>


 

//           {error && (

//             <div className="alert alert-danger text-center">{error}</div>

//           )}


 

//           <form onSubmit={handleSubmit}>

//             <div className="row g-3">


 

//               <div className="col-12">

//                 <label className="form-label small text-success fw-bold">

//                   FULL NAME

//                 </label>

//                 <input

//                   type="text"

//                   name="name"

//                   value={formData.name}

//                   onChange={handleChange}

//                   className="form-control bg-light text-black p-2 border-secondary rounded-3"

//                   placeholder="John Doe"

//                   required

//                 />

//               </div>


 

//               <div className="col-12">

//                 <label className="form-label small text-success fw-bold">

//                   EMAIL ADDRESS

//                 </label>

//                 <input

//                   type="email"

//                   name="email"

//                   value={formData.email}

//                   onChange={handleChange}

//                   className="form-control bg-light text-black p-2 border-secondary rounded-3"

//                   placeholder="john@gmail.com"

//                   required

//                 />

//               </div>


 

//               <div className="col-6">

//                 <label className="form-label small text-success fw-bold">D.O.B</label>

//                 <input

//                   type="date"

//                   name="dateOfBirth"

//                   value={formData.dateOfBirth}

//                   onChange={handleChange}

//                   style={{ colorScheme: "light" }}

//                   className="form-control bg-light text-black p-2 border-secondary rounded-3"

//                   required

//                 />

//               </div>


 

//               <div className="col-6">

//                 <label className="form-label small text-success fw-bold">GENDER</label>

//                 <select

//                   name="gender"

//                   value={formData.gender}

//                   onChange={handleChange}

//                   className="form-select bg-light text-black p-2 border-secondary rounded-3"

//                 >

//                   <option value="MALE">Male</option>

//                   <option value="FEMALE">Female</option>

//                   <option value="OTHER">Other</option>

//                 </select>

//               </div>


 

//               <div className="col-12">

//                 <label className="form-label small text-success fw-bold">

//                   CONTACT NO

//                 </label>

//                 <input

//                   type="tel"

//                   name="phone"

//                   value={formData.phone}

//                   onChange={handleChange}

//                   className="form-control bg-light text-black p-2 border-secondary rounded-3"

//                   placeholder="Enter your contact no..."

//                   required

//                 />

//               </div>


 

//               <div className="col-12 mb-3">

//                 <label className="form-label small text-success fw-bold">

//                   PASSWORD

//                 </label>

//                 <div className="input-group">

//                   <input

//                     type={showPassword ? "text" : "password"}

//                     name="password"

//                     value={formData.password}

//                     onChange={handleChange}

//                     className="form-control bg-light text-black p-2 border-secondary rounded-3"

//                     placeholder="Create Password"

//                     required

//                   />

//                   <button type="button"

//                     className="btn btn-outline-success"

//                     onClick={() => setShowPassword(!showPassword)}>

//                     {showPassword ? "Hide" : "Show"}

//                   </button>


 

//                 </div>

//               </div>


 

//             </div>


 

//             <button

//               type="submit"

//               className="btn btn-success w-100 py-3 rounded-pill fw-bold shadow-lg"

//             >

//               REGISTER ACCOUNT

//             </button>


 

//             <div className="text-center mt-3">

//               <button className="btn btn-link text-white text-decoration-none"

//                 onClick={onReturnToLogin}>

//                 Already have an account? <span className="text-success fw-bold opacity-100">Login</span>

//               </button>

//             </div>

//           </form>

//         </div>

//       </div>

//     </div>

//   );

// };


 

// export default PatientRegister;

import React, { useEffect, useState } from "react";


 

import axios from "axios";





 

interface Props {


 

  onRegisterSuccess: () => void;


 

  onReturnToLogin: () => void;


 

}





 

const PatientRegister: React.FC<Props> = ({ onRegisterSuccess, onReturnToLogin }) => {


 

  const [formData, setFormData] = useState({


 

    name: "",


 

    email: "",


 

    dateOfBirth: "",


 

    gender: "MALE",


 

    phone: "",


 

    password: "",


 

  });





 

  const [error, setError] = useState("");


 

  const [showPassword, setShowPassword] = useState(false);

  const[isValid,setIsValid]=useState(false);

  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  const phoneRegex=/^[0-9]{10}$/;

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;




 

  // --- FUNCTIONALITY (STRICTLY UNTOUCHED) ---


 

  const handleChange = (e: any) => {


 

    setFormData({ ...formData, [e.target.name]: e.target.value });


 

  };





 

  const handleSubmit = async (e: any) => {


 

    e.preventDefault();


 

    try {


 

      const response = await axios.post(


 

        "http://localhost:8080/clinicq/auth/signup",


 

        {


 

          name: formData.name,


 

          phone: formData.phone,


 

          dateOfBirth: formData.dateOfBirth,


 

          gender: formData.gender,


 

          user: {


 

            email: formData.email,


 

            password: formData.password,


 

          },


 

        }


 

      );


 

      console.log(response.data);


 

      onRegisterSuccess();


 

    } catch (err: any) {


 

      console.error(err);


 

      setError(err.response?.data || "Registration failed");


 

    }


 

  };

  const isValidAge=(dobString:string)=>{

    if(!dobString) return false;

    const today=new Date();

    const dob=new Date(dobString);

    let age=today.getFullYear()-dob.getFullYear();

    const m=today.getMonth()-dob.getMonth();

    if(m<0 || (m===0 && today.getDate()<dob.getDate())){

      age--;

    }

    return age>=12;

  };

  useEffect(()=>{

    const{name,email,dateOfBirth,phone,password}=formData;


 

    //age validation logic

   

    const isOldEnough=isValidAge(dateOfBirth);

    const nameRegex=/^[a-zA-Z\s]{2,50}$/;

    const isNameOk=nameRegex.test(name);

   

    const isEmailOk=emailRegex.test(email);

    const isPhoneOk=phoneRegex.test(phone);

    const isPassOk=passwordRegex.test(password);

    setIsValid(isNameOk && isOldEnough && isEmailOk && isPhoneOk && isPassOk);


 

  },[formData]);




 

  return (


 

    <div className="min-vh-100 d-flex align-items-center justify-content-center p-3"


 

      style={{


 

        background: 'radial-gradient(circle at center, #f8f9fa 0%, #e9ecef 100%)',


 

        fontFamily: "'Segoe UI', Roboto, sans-serif"


 

      }}>





 

      <style>


 

        {`


 

        @keyframes slideIn {


 

            from { opacity: 0; transform: translateY(10px); }


 

            to { opacity: 1; transform: translateY(0); }


 

        }


 

        .register-glass-card {


 

            background: rgba(255, 255, 255, 0.75);


 

            backdrop-filter: blur(30px);


 

            -webkit-backdrop-filter: blur(30px);


 

            border: 1px solid rgba(32, 201, 151, 0.2);


 

            animation: slideIn 0.5s ease-out;


 

            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);


 

        }


 

        .form-input-reg {


 

            background: #ffffff !important;


 

            border: 1px solid #dee2e6 !important;


 

            color: #212529 !important;


 

            transition: all 0.3s ease;


 

            padding: 10px 15px !important;


 

            font-weight: 500;


 

        }


 

        .form-input-reg:focus {


 

            border-color: #20c997 !important;


 

            box-shadow: 0 0 0 0.25rem rgba(32, 201, 151, 0.1) !important;


 

            outline: none;


 

        }


 

        .label-reg {


 

            font-size: 0.7rem;


 

            letter-spacing: 1px;


 

            color: #198754;


 

            margin-left: 4px;


 

            font-weight: 700;


 

        }


 

        .btn-emerald {


 

            background: linear-gradient(90deg, #20c997 0%, #00b894 100%);


 

            color: white; border: none; transition: transform 0.2s;


 

        }


 

        .btn-emerald:hover { transform: translateY(-2px); color: white; }


 

        `}


 

      </style>





 

      <div className="card register-glass-card rounded-5 border-0" style={{ maxWidth: '500px', width: '100%' }}>


 

        <div className="card-body p-4 p-md-5">




 

          {/* Top Branding Section */}


 

          <div className="text-center mb-4">


 

            <div className="mx-auto mb-3 d-flex align-items-center justify-content-center shadow-sm"


 

              style={{


 

                width: '54px', height: '54px', borderRadius: '16px',


 

                background: 'linear-gradient(135deg, #20c997, #00b894)', color: '#fff'


 

              }}>


 

              <span style={{ fontSize: '1.6rem' }}>🏥</span>


 

            </div>


 

            <h2 className="fw-bold mb-1" style={{ color: '#111', letterSpacing: '-0.5px' }}>JOIN CLINICQ</h2>


 

            <p className="small text-muted fw-bold opacity-75">Create your patient profile in seconds</p>


 

          </div>





 

          {error && (


 

            <div className="alert alert-danger text-center py-2 small border-0 bg-danger-subtle text-danger fw-bold rounded-3">


 

              {error}


 

            </div>


 

          )}





 

          <form onSubmit={handleSubmit}>


 

            <div className="row g-3">


 

              <div className="col-12">


 

                <label className="label-reg text-uppercase">Full Name</label>


 

                <input type="text" name="name" value={formData.name} onChange={handleChange} className={`form-control form-input-reg rounded-3 shadow-none ${formData.name && !/^[a-zA-Z\s]{2,50}$/.test(formData.name)?'border-danger':''}`} placeholder="John Doe" required />


 

                {formData.name && !/^[a-zA-Z\s]{2,50}$/.test(formData.name) && (

                  <small className="text-danger ms-1" style={{fontSize:'10px'}}>

                    * Name should be at least 2 letters (no numbers or symbols).

                  </small>

                )}


 

              </div>





 

              <div className="col-12">


 

                <label className="label-reg text-uppercase">Email Address</label>


 

                <input type="email" name="email" value={formData.email} onChange={handleChange} className="form-control form-input-reg rounded-3 shadow-none" placeholder="john@example.com" required />

                {formData.email && !emailRegex.test(formData.email) && (

                  <small className="text-danger ms-1" style={{fontSize:'10px'}}>* Please enter valid email</small>

                )}


 

              </div>





 

              <div className="col-6">


 

                <label className="label-reg text-uppercase">D.O.B</label>


 

                <input type="date" name="dateOfBirth" value={formData.dateOfBirth} onChange={handleChange}

                max={new Date().toISOString().split("T")[0]}

                 className={`form-control form-input-reg rounded-3 shadow-none ${formData.dateOfBirth && !isValidAge(formData.dateOfBirth)?'border-danger':''}`}

                  style={{ colorScheme: "light" }} required />

                  {formData.dateOfBirth && !isValidAge(formData.dateOfBirth) && (

                    <small className="text-danger ms-1" style={{ fontSize:'9px'}}>

                      * Patient must be atleast 12 years old to register.

                    </small>

                  )}


 

              </div>





 

              <div className="col-6">


 

                <label className="label-reg text-uppercase">Gender</label>


 

                <select name="gender" value={formData.gender} onChange={handleChange} className="form-select form-input-reg rounded-3 shadow-none">


 

                  <option value="MALE">Male</option>


 

                  <option value="FEMALE">Female</option>


 

                  <option value="OTHER">Other</option>


 

                </select>


 

              </div>





 

              <div className="col-12">


 

                <label className="label-reg text-uppercase">Contact No</label>


 

                <input type="tel" name="phone" value={formData.phone} onChange={handleChange} maxLength={10} className="form-control form-input-reg rounded-3 shadow-none" placeholder="+91..." required />

                {formData.phone && !phoneRegex.test(formData.phone) && (

                  <small className="text-danger ms-1" style={{fontSize:'10px'}}>* Enter 10 digits</small>

                )}


 

              </div>





 

              <div className="col-12 mb-2">


 

                <label className="label-reg text-uppercase">Secure Password</label>


 

                <div className="input-group">


 

                  <input


 

                    type={showPassword ? "text" : "password"}


 

                    name="password"


 

                    value={formData.password}


 

                    onChange={handleChange}


 

                    className="form-control form-input-reg rounded-start-3 shadow-none border-end-0"


 

                    placeholder="••••••••"


 

                    required


 

                  />

                 


 

                  <button


 

                    type="button"


 

                    className="btn bg-white border border-start-0"


 

                    style={{ borderColor: '#dee2e6', borderRadius: '0 8px 8px 0' }}


 

                    onClick={() => setShowPassword(!showPassword)}


 

                  >


 

                    <small className="fw-bold text-success" style={{ fontSize: '10px' }}>{showPassword ? "HIDE" : "SHOW"}</small>


 

                  </button>


 

                 


 

                </div>

                {formData.password && !passwordRegex.test(formData.password) && (

                    <small className="text-danger ms-1" style={{fontSize:'10px',display:'block'}}>

                      * Use 8+ characters with letters,numbers and special characters.

                    </small>

                  )}


 

              </div>


 

            </div>





 

            <button type="submit"

            disabled={!isValid}

            className="btn btn-emerald w-100 py-3 rounded-pill fw-bold shadow-lg mt-4 text-uppercase ls-1"

            style={{

              background:!isValid?'#adb5bd':'linear-gradient(90deg,#20c997 0%,#00b894 100%)',

              cursor:!isValid?'not-allowed':'pointer',

              opacity:!isValid?0.7:1

            }}>


 

              REGISTER ACCOUNT


 

            </button>





 

            <div className="text-center mt-4">


 

              <button type="button" className="btn btn-link text-decoration-none small fw-bold p-0"


 

                style={{ color: '#6c757d', fontSize: '13px' }}


 

                onClick={onReturnToLogin}>


 

                Already have an account? <span style={{ color: '#20c997' }}>Login</span>


 

              </button>


 

            </div>


 

          </form>


 

        </div>


 

      </div>


 

    </div>


 

  );


 

};





 

export default PatientRegister;