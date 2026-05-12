import React, { useState } from 'react';

interface Patient {
  id: string;
  name: string;
  age: number;
  contact: string;
  reason: string;
  queueNumber: string;
  doctor?: string;
  timestamp: Date;
  type: 'walk-in' | 'appointment';
  appointmentId?: string;
  status: 'Waiting' | 'In Consultation' | 'Completed' | 'No Show';
  position: number; 
  arrivalTime?: string;
}

interface Appointment {
  id: string;
  patientName: string;
  age: number;
  contact: string;
  doctorId: string;
  doctorName: string;
  appointmentTime: string;
  date: string;
  status: 'Scheduled' | 'Checked-In' | 'Completed' | 'Cancelled' | 'No Show';
  reason?: string;
  queueNumber?: string;
}

interface Doctor {
  id: string;
  name: string;
  specialization: string;
}

interface DoctorDashboardNewProps {
  doctors: Doctor[];
  appointments: Appointment[];
  patients: Patient[];
  selectedDoctor?: Doctor;
}

const DoctorDashboardNew: React.FC<DoctorDashboardNewProps> = ({ doctors, appointments, patients, selectedDoctor }) => {
  const [currentTime, setCurrentTime] = useState(new Date());

  // Filter appointments and patients for selected doctor
  const filteredAppointments = appointments.filter((appointment) => appointment.doctorId === selectedDoctor?.id);
  const filteredPatients = patients.filter((patient) => patient.doctor === selectedDoctor?.id);

  const currentQueue = filteredPatients.sort((a, b) => a.position - b.position);

  const nextPatient = currentQueue.find((patient) => patient.status === 'Waiting');

  const handleStartConsultation = (patientId: string) => {
    console.log(`Starting consultation with patient ${patientId}`);
    // In real app, this would update patient status
  };

  const handleNoShow = (patientId: string) => {
    console.log(`Marking patient ${patientId} as no show`);
    // In real app, this would update patient status
  };

  const getStatusBadgeClass = (status: string) => {
    switch (status) {
      case 'Scheduled':
        return 'bg-warning';
      case 'Checked-In':
        return 'bg-info';
      case 'Completed':
        return 'bg-success';
      case 'Cancelled':
        return 'bg-danger';
      case 'No Show':
        return 'bg-secondary';
      default:
        return 'bg-secondary';
    }
  };

  return (
    <div className="container-fluid bg-light min-vh-100 p-4">
      {/* Header */}
      <div className="row mb-4">
        <div className="col-12">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h1 className="text-primary mb-0">Good morning, Dr. Rao</h1>
              <p className="text-muted mb-0">Your appointments and queue for today</p>
            </div>
            <div className="text-end">
              <div className="text-primary">{new Date().toLocaleDateString()}</div>
              <div className="text-muted">{new Date().toLocaleTimeString()}</div>
            </div>
          </div>
        </div>
      </div>

      <div className="row">
        {/* Today's Schedule - Left */}
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-primary text-white">
              <h5 className="mb-0">Today's Schedule</h5>
              <div className="d-flex justify-content-between align-items-center">
                <span>Appointments: 18</span>
                <span>Walk-ins: 7</span>
                <span>Waiting: 4</span>
              </div>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead>
                    <tr>
                      <th>Time</th>
                      <th>Patient</th>
                      <th>Visitor</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredAppointments.map((appointment: Appointment) => (
                      <tr key={appointment.id}>
                        <td className="fw-bold">{appointment.appointmentTime}</td>
                        <td>{appointment.patientName}</td>
                        <td>{appointment.age}</td>
                        <td>
                          <span className={`badge ${getStatusBadgeClass(appointment.status)}`}>
                            {appointment.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        {/* Current Queue - Top Right */}
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm mb-3">
            <div className="card-header bg-success text-white">
              <h5 className="mb-0">Current Queue</h5>
              <div className="d-flex justify-content-between align-items-center">
                <span>Next Patient</span>
                <span>Queue: {currentQueue.length}</span>
              </div>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead>
                    <tr>
                      <th>Queue #</th>
                      <th>Name</th>
                      <th>Arrival</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredPatients.map((patient, index) => (
                      <tr key={patient.id} className={patient === nextPatient ? 'table-success' : ''}>
                        <td className="fw-bold">{patient.queueNumber}</td>
                        <td>{patient.name}</td>
                        <td>{patient.arrivalTime || new Date(patient.timestamp).toLocaleTimeString()}</td>
                        <td>
                          <span className={`badge ${getStatusBadgeClass(patient.status)}`}>
                            {patient.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        {/* Next Patient Details - Middle Right */}
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-info text-white">
              <h5 className="mb-0">Next Patient</h5>
            </div>
            <div className="card-body text-center">
              {nextPatient ? (
                <>
                  <h4 className="text-info">{nextPatient.queueNumber}</h4>
                  <h3 className="text-primary">{nextPatient.name}</h3>
                  <p className="text-muted">Arrival: {nextPatient.arrivalTime}</p>
                  <div className="d-flex gap-2 justify-content-center mt-3">
                    <button 
                      className="btn btn-success btn-lg"
                      onClick={() => handleStartConsultation(nextPatient.id)}
                    >
                      Start Consultation
                    </button>
                    <button 
                      className="btn btn-outline-danger btn-lg"
                      onClick={() => handleNoShow(nextPatient.id)}
                    >
                      No Show
                    </button>
                  </div>
                </>
              ) : (
                <p className="text-muted">No patients in queue</p>
              )}
            </div>
          </div>
        </div>

        {/* Today's Schedule (Bottom) */}
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-primary text-white">
              <h5 className="mb-0">Today's Schedule</h5>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-sm">
                  <thead>
                    <tr>
                      <th>Time</th>
                      <th>Patient</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredAppointments.slice(3).map((appointment: Appointment) => (
                      <tr key={appointment.id}>
                        <td className="fw-bold">{appointment.appointmentTime}</td>
                        <td>{appointment.patientName}</td>
                        <td>
                          <span className={`badge ${getStatusBadgeClass(appointment.status)}`}>
                            {appointment.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        {/* Completed Today */}
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-success text-white">
              <h5 className="mb-0">Completed Today</h5>
            </div>
            <div className="card-body">
              <div className="row">
                <div className="col-md-6 text-center">
                  <h2 className="text-success">8</h2>
                  <p className="text-muted">Consultations</p>
                </div>
                <div className="col-md-6 text-center">
                  <h2 className="text-primary">2</h2>
                  <p className="text-muted">Pending</p>
                </div>
              </div>
              <div className="mt-3">
                <div className="d-flex justify-content-between">
                  <span className="text-muted">Last patient seen at 11:45 AM</span>
                  <button className="btn btn-outline-primary btn-sm">View Details</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboardNew;
