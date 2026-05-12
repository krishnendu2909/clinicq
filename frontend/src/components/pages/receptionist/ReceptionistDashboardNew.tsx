import React, { useState } from 'react';

interface Appointment {
  id: string;
  patientName: string;
  age: number;
  contact: string;
  doctorId: string;
  doctorName: string;
  appointmentTime: string;
  date: string;
  status: 'Scheduled' | 'Checked-In' | 'Completed' | 'Cancelled' | 'Booked';
  reason?: string;
  queueNumber?: string;
}

interface User {
  id: string;
  name: string;
  role: string;
  flatNo: string;
  status: 'Active' | 'Blocked' | 'Completed' | 'Cancelled';
}

interface Doctor {
  id: string;
  name: string;
  specialization: string;
}

const ReceptionistDashboardNew: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'book-appointment' | 'my-appointments' | 'users'>('book-appointment');

  const getStatusBadgeClass = (status: string) => {
    switch (status) {
      case 'Scheduled':
      case 'Booked':
        return 'bg-warning';
      case 'Checked-In':
        return 'bg-info';
      case 'Completed':
        return 'bg-success';
      case 'Cancelled':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  };
  
  // Sample data
  const doctors: Doctor[] = [
    { id: 'dr1', name: 'Dr. Angela Lee', specialization: 'Cardiology' },
    { id: 'dr2', name: 'Dr. Sarah Wong', specialization: 'General Practice' },
    { id: 'dr3', name: 'Dr. Brian Tan', specialization: 'Pediatrics' }
  ];

  const [selectedDoctor, setSelectedDoctor] = useState(doctors[0].id);
  const [selectedDate, setSelectedDate] = useState('2026-06-22');
  const [selectedTime, setSelectedTime] = useState('10:00');
  const [patientName, setPatientName] = useState('');
  const [patientPhone, setPatientPhone] = useState('');
  const [patientReason, setPatientReason] = useState('');
  const [confirmedAppointment, setConfirmedAppointment] = useState(false);
  const [newAppointmentTime, setNewAppointmentTime] = useState('2:00 PM');

  const timeSlots = [
    '10:00', '10:15', '10:30', '10:45', '10:50',
    '11:00', '11:15', '11:30', '11:45',
    '13:00', '13:30', '14:00', '14:30', '15:00',
    '15:30', '16:00', '16:15', '16:30', '16:45',
    '17:00', '17:30', '17:45', '18:00'
  ];

  const appointments: Appointment[] = [
    {
      id: '1',
      patientName: 'John Smith',
      age: 35,
      contact: '555-0101',
      doctorId: 'dr1',
      doctorName: 'Dr. Angela Lee',
      appointmentTime: '10:00',
      date: '2026-06-22',
      status: 'Checked-In',
      reason: 'Regular checkup',
      queueNumber: 'D001'
    },
    {
      id: '2',
      patientName: 'Emily Chen',
      age: 28,
      contact: '555-0102',
      doctorId: 'dr2',
      doctorName: 'Dr. Sarah Wong',
      appointmentTime: '14:00',
      date: '2026-06-22',
      status: 'Booked',
      reason: 'Follow-up consultation'
    },
    {
      id: '3',
      patientName: 'Michael Davis',
      age: 42,
      contact: '555-0103',
      doctorId: 'dr3',
      doctorName: 'Dr. Brian Tan',
      appointmentTime: '11:45',
      date: '2026-06-18',
      status: 'Completed',
      reason: 'Vaccination'
    }
  ];

  const users: User[] = [
    {
      id: '1',
      name: 'Amanda Lim',
      role: 'Resident',
      flatNo: '03',
      status: 'Blocked'
    },
    {
      id: '2',
      name: 'Brian Tan',
      role: 'Resident',
      flatNo: '105',
      status: 'Completed'
    },
    {
      id: '3',
      name: 'Sarah Wong',
      role: 'Resident',
      flatNo: 'Bk 02',
      status: 'Cancelled'
    }
  ];

  const handleConfirmAppointment = () => {
    if (patientName && patientPhone && selectedDoctor && selectedDate && selectedTime) {
      setConfirmedAppointment(true);
      // In real app, this would save to backend
      console.log('Appointment confirmed:', {
        patient: patientName,
        phone: patientPhone,
        doctor: selectedDoctor,
        date: selectedDate,
        time: selectedTime,
        reason: patientReason
      });
    }
  };

  const handleReschedule = (appointmentId: string) => {
    console.log('Rescheduling appointment:', appointmentId);
    // Implementation would open reschedule modal
  };

  const handleCancel = (appointmentId: string) => {
    console.log('Cancelling appointment:', appointmentId);
    // Implementation would cancel appointment
  };

  const handleUserAction = (userId: string, action: string) => {
    console.log(`User ${userId}: ${action}`);
    // Implementation would handle user status changes
  };

  return (
    <div className="container-fluid bg-light min-vh-100 p-4">
      <div className="row">
        {/* Left Panel - Book Appointment */}
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-primary text-white">
              <h5 className="mb-0">Book Appointment</h5>
            </div>
            <div className="card-body">
              <div className="row">
                <div className="col-md-6">
                  <label className="form-label">Select Doctor</label>
                  <select 
                    className="form-select"
                    value={selectedDoctor}
                    onChange={(e) => setSelectedDoctor(e.target.value)}
                  >
                    {doctors.map(doctor => (
                      <option key={doctor.id} value={doctor.id}>
                        {doctor.name} - {doctor.specialization}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="col-md-6">
                  <label className="form-label">Date</label>
                  <input 
                    type="date" 
                    className="form-control"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                  />
                </div>
              </div>

              <div className="row mt-3">
                <div className="col-md-6">
                  <label className="form-label">Time Slots</label>
                  <div className="row g-2">
                    {timeSlots.slice(0, 12).map(time => (
                      <div key={time} className="col-3">
                        <button 
                          className={`btn w-100 ${selectedTime === time ? 'btn-primary' : 'btn-outline-secondary'}`}
                          onClick={() => setSelectedTime(time)}
                        >
                          {time}
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
                <div className="col-md-6">
                  <label className="form-label">Selected Time</label>
                  <div className="form-control bg-light">
                    {selectedTime || 'Select a time slot'}
                  </div>
                </div>
              </div>

              <div className="row mt-3">
                <div className="col-12">
                  <label className="form-label">Patient Details</label>
                  <div className="row">
                    <div className="col-md-4">
                      <input 
                        type="text" 
                        className="form-control"
                        placeholder="Name"
                        value={patientName}
                        onChange={(e) => setPatientName(e.target.value)}
                      />
                    </div>
                    <div className="col-md-4">
                      <input 
                        type="tel" 
                        className="form-control"
                        placeholder="Phone"
                        value={patientPhone}
                        onChange={(e) => setPatientPhone(e.target.value)}
                      />
                    </div>
                    <div className="col-md-4">
                      <input 
                        type="text" 
                        className="form-control"
                        placeholder="Reason for visit (optional)"
                        value={patientReason}
                        onChange={(e) => setPatientReason(e.target.value)}
                      />
                    </div>
                  </div>
                </div>

              </div>

              <div className="text-center mt-3">
                <button 
                  className="btn btn-primary btn-lg"
                  onClick={handleConfirmAppointment}
                  disabled={!patientName || !patientPhone || !selectedDoctor || !selectedDate || !selectedTime}
                >
                  Confirm Appointment
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Right Panel */}
        <div className="col-lg-4">
          {/* Confirm Appointment Panel */}
          {confirmedAppointment && (
            <div className="card border-0 shadow-sm mb-3">
              <div className="card-header bg-success text-white">
                <h6 className="mb-0">Confirm Appointment</h6>
              </div>
              <div className="card-body">
                <div className="alert alert-success">
                  <i className="fas fa-check-circle me-2"></i>
                  Your appointment is confirmed!
                </div>
                <div className="text-center">
                  <strong>Appointment {Math.floor(Math.random() * 1000)}</strong>
                  <br />
                  <span className="text-muted">{newAppointmentTime} - {newAppointmentTime}</span>
                </div>
                <div className="mt-3">
                  <label className="form-label">New appointment time</label>
                  <select 
                    className="form-select"
                    value={newAppointmentTime}
                    onChange={(e) => setNewAppointmentTime(e.target.value)}
                  >
                    <option value="2:00 PM - 2:15 PM">2:00 PM - 2:15 PM</option>
                    <option value="2:15 PM - 2:30 PM">2:15 PM - 2:30 PM</option>
                    <option value="2:30 PM - 2:45 PM">2:30 PM - 2:45 PM</option>
                    <option value="3:00 PM - 3:15 PM">3:00 PM - 3:15 PM</option>
                  </select>
                </div>
              </div>
            </div>
          )}

          {/* My Appointments */}
          <div className="card border-0 shadow-sm mb-3">
            <div className="card-header bg-info text-white">
              <h6 className="mb-0">My Appointments</h6>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead>
                    <tr>
                      <th>Patient</th>
                      <th>Doctor</th>
                      <th>Date</th>
                      <th>Time</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {appointments.map((appointment) => (
                      <tr key={appointment.id}>
                        <td>{appointment.patientName}</td>
                        <td>{appointment.doctorName}</td>
                        <td>{appointment.date}</td>
                        <td>{appointment.appointmentTime}</td>
                        <td>
                          <span className={`badge ${getStatusBadgeClass(appointment.status)}`}>
                            {appointment.status}
                          </span>
                        </td>
                        <td>
                          <div className="btn-group btn-group-sm">
                            {['Scheduled', 'Booked'].includes(appointment.status) && (
                              <button 
                                className="btn btn-outline-primary btn-sm"
                                onClick={() => handleReschedule(appointment.id)}
                              >
                                Reschedule
                              </button>
                            )}
                            <button 
                              className="btn btn-outline-danger btn-sm"
                              onClick={() => handleCancel(appointment.id)}
                            >
                              Cancel
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Users */}
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-secondary text-white">
              <h6 className="mb-0">Users</h6>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Role</th>
                      <th>Flat No.</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr key={user.id}>
                        <td>{user.name}</td>
                        <td>{user.role}</td>
                        <td>{user.flatNo}</td>
                        <td>
                          <span className={`badge ${
                            user.status === 'Active' ? 'bg-success' :
                            user.status === 'Blocked' ? 'bg-danger' :
                            user.status === 'Completed' ? 'bg-primary' :
                            'bg-secondary'
                          }`}>
                            {user.status}
                          </span>
                        </td>
                        <td>
                          <div className="btn-group btn-group-sm">
                            <button 
                              className="btn btn-outline-secondary btn-sm"
                              onClick={() => handleUserAction(user.id, 'edit')}
                            >
                              Edit
                            </button>
                            <button 
                              className="btn btn-outline-primary btn-sm"
                              onClick={() => handleUserAction(user.id, 'activate')}
                            >
                              Activate
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReceptionistDashboardNew;
