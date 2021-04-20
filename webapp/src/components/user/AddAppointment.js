import React, {useEffect, useRef, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {useParams} from 'react-router';
import {Button, TextField, CircularProgress, makeStyles} from '@material-ui/core';
import Box from '@material-ui/core/Box';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import ReactDOM from 'react-dom';

import {api, server} from '../../endpoints/server';
import Navbar from '../subcomponents/Navbar';
import BackNav from '../subcomponents/BackNav';
import AppointmentList from '../subcomponents/AppointmentList'
import ResourceList from '../subcomponents/ResourceList'
import { dateConverter } from '../../utilities/dateUtils';


const useStyles = makeStyles({
    apptCalendar: {
        margin: 'auto'
    },
    openAppt: {
        backgroundColor: '#FFFFFF',
        '&:hover': {
            backgroundColor: '#E6E6E6 !important' // Needed bc react-calendar has universal hover that overrides
        }
    },
    closedAppt: {
        backgroundColor: '#3F51B5',
        color: '#FFFFFF',
        '&:hover': {
            backgroundColor: '#7885CB !important' // Needed bc react-calendar has universal hover that overrides
        }
    }
});

function AddAppointment(props) {
    const param = useParams();

    const [showConfirmation, setShowConfirmation] = useState(false);
    const [newAppointmentId, setNewAppointmentId] = useState(false);
    const [appointment, setAppointment] = useState(null)
    const [selectedResource, setSelectedResource] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [appointmentDate, setAppointmentDate] = useState(null)
    const minDate = useRef(new Date())
    const maxDate = useRef(new Date())

    useEffect(() => {
        minDate.current.setDate(minDate.current.getDate() + 1);
        maxDate.current.setDate(maxDate.current.getDate() + 31);
    }, [])

    useEffect(() => {
        //Updating an existing appointment
        if (param.id){
            const config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'email': props.user.email,
                    'x-api-key': props.user.token
                }
            }
    
            server.get(api.getAppointmentDetails + "/" + param.id, config)
                .then(res => {
                    setAppointment(res.data);
                    setSelectedResource(res.data.resourceId);
                    let date = dateConverter.fromSQLDateString(res.data.date);
                    setAppointmentDate(date);
                    setMessage(res.data.message);
                })
                .catch(() => {
                    console.log("ERROR")
                });
        }
    }, [param.id])

    useEffect(() => {
        setShowConfirmation(true);
    }, [newAppointmentId]);

    const onSelectAppointmentCallBack = (selectedAppointmentId) => {
        setShowConfirmation(true);
        setNewAppointmentId(selectedAppointmentId);
    }

    const onConfirmAppointment = () => {
        const bookAppointment = () => {
            const params = new URLSearchParams();
            params.append('appointmentId', newAppointmentId);
            params.append('message', message);

            console.log("Book appointment params", params)

            const config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'email': props.user.email,
                    'x-api-key': props.user.token
                },

            }

            server.post(api.bookAppointment, params, config)
                .then(res => {
                    console.log("Success");
                })
                .catch(() => {
                    console.log("Error")
                })
        };


        bookAppointment();
    }

    const onUpdateAppointment = () => {
        const updateAppointment = () => {
            const params = {
                'appointmentId': appointment.appointmentId,
                'newAppointmentId': newAppointmentId,
                'message': message
            }

            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'email': props.user.email,
                    'x-api-key': props.user.token
                },
            }

            server.put(api.updateAppointment, params, config)
                .then(res => {
                    console.log("Success");
                })
                .catch(() => {
                    console.log("Error")
                })
        };


        updateAppointment();
    }

    const onSelectResourceCallBack = (newSelectedResource) => {
        setSelectedResource(newSelectedResource);
    }

    const renderAddAppointment = () => {
        return <div>
                    <h3>Add Appointment</h3>
                        
                    <Box mb={2}>
                        <h6>Person/facility for your appointment: </h6>
                        <ResourceList user={props.user} onSelectResourceCallBack={onSelectResourceCallBack} selectedResource={selectedResource} />
                    </Box>
                    
                    <Box mb={2}>
                        <h6>An appointment date: </h6>
                        <div id="appointmentList">
                            <AppointmentList user={props.user} resourceId={selectedResource} onSelectAppointmentCallBack={onSelectAppointmentCallBack} selectedDate={appointmentDate} selectedDate={appointmentDate} minDate={minDate.current} maxDate={maxDate.current} />
                        </div>
                    </Box>

                    <Box mb={2}>
                        <h6>Message for us (optional):</h6>
                        <div id="message">
                            <TextField variant="outlined" size="medium" multiline={true}  type="message" label="Message" value={message} error={message !== ''} helperText={error}
                                onChange={(e) => setMessage(e.currentTarget.value)} />
                        </div>
                    </Box>
                    
                    <Box mb={2}>
                        <div style={{display: showConfirmation ? "block" : "none"}}>
                            <Button variant="contained" color="primary" onClick={onConfirmAppointment} >Book appointment</Button>
                        </div>
                    </Box>
                </div>
    }

    const renderUpdateAppointment = () => {
        console.log("Resource id", appointment.resourceId)
        return <div>
                    <h3>Update Appointment</h3>
                        
                    <Box mb={2}>
                        <h6>Person/facility for your appointment: </h6>
                        <ResourceList user={props.user} onSelectResourceCallBack={onSelectResourceCallBack} selectedResource={selectedResource}/>
                    </Box>
                    
                    <Box mb={2}>
                        <h6>An appointment date: </h6>
                        <div id="appointmentList"></div>
                    </Box>

                    <AppointmentList user={props.user} resourceId={selectedResource} onSelectAppointmentCallBack={onSelectAppointmentCallBack} selectedDate={appointmentDate} minDate={minDate.current} maxDate={maxDate.current}/>

                    <Box mb={2}>
                        <h6>Message for us (optional):</h6>
                        <div id="message">
                            <TextField variant="outlined" size="medium" multiline={true}  type="message" label="Message" value={appointment.message} error={appointment.message !== ''} helperText={error}
                                onChange={(e) => setMessage(e.currentTarget.value)} />
                        </div>
                    </Box>
                    
                    <Box mb={2}>
                        <div style={{display: showConfirmation ? "block" : "none"}}>
                            <Button variant="contained" color="primary" onClick={onConfirmAppointment} >Book appointment</Button>
                        </div>
                    </Box>
                </div>
    }

    const renderAppointmentForm = () => {
        return <div>
                    <h3>{param.id ? "Update Appointment" : "Add Appointment"}</h3>
                        
                    <Box mb={2}>
                        <h6>Person/facility for your appointment: </h6>
                        <ResourceList user={props.user} onSelectResourceCallBack={onSelectResourceCallBack} selectedResource={selectedResource} />
                    </Box>
                    
                    <Box mb={2}>
                        <h6>Appointment date: </h6>
                        <div id="appointmentList">
                        <AppointmentList user={props.user} resourceId={selectedResource} onSelectAppointmentCallBack={onSelectAppointmentCallBack} selectedDate={appointmentDate} selectedDate={appointmentDate} minDate={minDate.current} maxDate={maxDate.current} />
                        </div>
                    </Box>

                    <Box mb={2}>
                        <h6>Message for us (optional):</h6>
                        <div id="message">
                            <TextField variant="outlined" size="medium" multiline={true}  type="message" label="Message" value={message} error={message !== ''} helperText={error}
                                onChange={(e) => setMessage(e.currentTarget.value)} />
                        </div>
                    </Box>
                    
                    <Box mb={2}>
                        <div style={{display: showConfirmation ? "block" : "none"}}>
                            <Button variant="contained" color="primary" onClick={onConfirmAppointment} style={{display: param.id ? "none" : "" }}>Book appointment</Button>
                            <Button variant="contained" color="primary" onClick={onUpdateAppointment} style={{display: param.id ? "" : "none" }}>Update appointment</Button>
                        </div>
                    </Box>
                </div>

    }
    

    return (
        <React.Fragment>
            <Navbar user={props.user} customer />
            <BackNav />
            
            <div className="text-center">
                {renderAppointmentForm()}
            </div>
        </React.Fragment>
    );
}

export default AddAppointment;
