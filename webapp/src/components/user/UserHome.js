import React, {useEffect, useRef, useState} from 'react';
import LogoutButton from '../subcomponents/LogoutButton';
import Navbar from '../subcomponents/Navbar';
import {api, server} from '../../endpoints/server';
import AppointmentDetails from '../subcomponents/AppointmentDetails';
import {Button, Box} from '@material-ui/core';


function UserHome(props) {
    const [appointments, setAppointments] = useState([]);

    const getAppointments = () => {
        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'email': props.user.email,
                'x-api-key': props.user.token
            }
        }

        server.get(api.getUserAppointments, config)
            .then(res => {
                setAppointments(res.data);
                console.log(res.data);
            })
            .catch(() => setAppointments(null));
    }

    useEffect(() => {
        getAppointments()
    }, [props]);

    const renderAppointments = () => {
        console.log("rendering appointments");
        let appoinmentsDetailsComponents = appointments.map(function(appointment){
            return (<Box mb={2}>
                    <AppointmentDetails appointment={appointment} user={props.user} onDeleteAppointmentCallBack={onDeleteAppointmentCallBack} />
            </Box>);
          })
        return <div>
            {appoinmentsDetailsComponents}
        </div>
    }

    const onDeleteAppointmentCallBack = (deletedAppointmentId) => {
        getAppointments();
    }

    return (
        <React.Fragment>
            <Navbar user={props.user} />
            <div id="home" className="text-center">
                <h3>Your booked appointments</h3>
                {renderAppointments()}
            </div>
        </React.Fragment>
    );
}

export default UserHome;
