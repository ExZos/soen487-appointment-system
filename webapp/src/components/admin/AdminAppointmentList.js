import {useEffect, useRef, useState} from 'react';

import {api, server} from '../../endpoints/server';
import {CircularProgress} from '@material-ui/core';
import {useLocation} from 'react-router';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

// USED FOR TEMPORARY HARDCODING
const appts = [
    {
        id: 1
    }, {
        id: 2
    }, {
        id: 3
    }
];

function AdminAppointmentList(props) {
    const location = useLocation();

    const [appointments, setAppointments] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);

    const resource = useRef(JSON.parse(location.state.resource));
    const currentDate = new Date();

    useEffect(() => {
        const getAppointmentList = () => {
            server.get(api.listResourceAppointments)
                .then(res => setAppointments(res.data))
                .catch(() => setAppointments(appts)) // TEMPORARY HARDCODING
                .finally(() => setIsLoaded(true));
        };

        getAppointmentList();
    }, [])

    const renderAppointmentList = () => {
        if(!isLoaded)
            return <CircularProgress />;
        else if(!appointments)
            return "An error occurred while getting the appointments";
        else if(appointments.length === 0)
            return "There are no appointments";

        const minDate = new Date();
        minDate.setDate(currentDate.getDate() + 1);

        const maxDate = new Date();
        maxDate.setDate(currentDate.getDate() + 30)

        return (
            <Calendar minDate={minDate} maxDate={maxDate} />
        );
    };

    return (
        <div id="adminAppointmentList" className="text-center">
            <h3>Appointments</h3>
            <div>for <i>{resource.current.name}</i></div>

            <div className="appointmentList mt-3">
                {renderAppointmentList()}
            </div>
        </div>
    );
}

export default AdminAppointmentList;
