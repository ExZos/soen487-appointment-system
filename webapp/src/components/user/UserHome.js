import React, {useEffect, useRef, useState} from 'react';
import LogoutButton from '../subcomponents/LogoutButton';
import Navbar from '../subcomponents/Navbar';
import {api, server} from '../../endpoints/server';
import AppointmentDetails from '../subcomponents/AppointmentDetails';
import {Button, Box} from '@material-ui/core';


function UserHome(props) {
    const [appointments, setAppointments] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);


    useEffect(() => {
        console.log(props);
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
                /*apptDict.current = res.data.reduce((a,x) => ({...a, [x.date]: {
                    id: x.appointmentId,
                    status: x.status
                }}), {});*/
            })
            .catch(() => setAppointments(null))
            .finally(() => setIsLoaded(true));
    }, [props]);

    const renderAppointments = () => {
        console.log("rendering appointments");
        let appoinmentsDetailsComponents = appointments.map(function(appointment){
            return (<Box mb={2}>
                    <AppointmentDetails appointment={appointment} />
            </Box>);
          })
        return <div>
            {appoinmentsDetailsComponents}
        </div>
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
