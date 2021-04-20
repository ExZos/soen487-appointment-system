import React, {useEffect, useRef, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {CircularProgress, makeStyles} from '@material-ui/core';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

import {api, server} from '../../endpoints/server';
import {dateFormatter} from '../../utilities/dateUtils';
import Navbar from '../subcomponents/Navbar';
import BackNav from '../subcomponents/BackNav';
import ResourceList from '../subcomponents/ResourceList'
import AppointmentList from '../subcomponents/AppointmentList';

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
    const appointmentList = useRef(null);

    /*const classes = useStyles();

    const location = useLocation();
    const history = useHistory();

    const [appointments, setAppointments] = useState([]);
    const apptDict = useRef({});
    const [isLoaded, setIsLoaded] = useState(false);

    const resource = useRef(JSON.parse(location.state?.resource));
    const minDate = useRef(new Date());
    const maxDate = useRef(new Date());

    useEffect(() => {
        if(!location.state)
            history.push('/admin/home');

        const getAppointmentList = () => {
            const config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'username': props.user.username,
                    'x-api-key': props.user.token
                }
            }

            server.get(api.listResourceAppointments + '/' + resource.current.resourceId, config)
                .then(res => {
                    setAppointments(res.data);
                    apptDict.current = res.data.reduce((a,x) => ({...a, [x.date]: {
                        id: x.appointmentId,
                        status: x.status
                    }}), {});
                })
                .catch(() => setAppointments(null))
                .finally(() => setIsLoaded(true));
        };

        minDate.current.setDate(minDate.current.getDate() + 1);
        maxDate.current.setDate(maxDate.current.getDate() + 31);

        getAppointmentList();
    }, [history, location.state, props.user])

    const redirectAppointmentDetails = (date) => {
        const appointment = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date)];
        if(!appointment) {
            alert("No appointment on this day");
            return;
        }

        history.push({
            pathname: '/admin/appointment/' + appointment.id
        });
    };

    const disableCalendarDays = (date) => {
        // Disable days that don't have appointment
        const appointment = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date.date)];
        return !Boolean(appointment);
    };

    const markCalendarDays = (date) => {
        const appointment = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date.date)];

        if(!appointment)
            return;
        else if(appointment.status === 'OPEN')
            return classes.openAppt;

        return classes.closedAppt;
    };

    const renderAppointmentList = () => {
        if(!isLoaded)
            return <CircularProgress />;
        else if(!appointments)
            return "An error occurred while getting the appointments";

        return (
            <Calendar
                className={classes.apptCalendar}
                calendarType="US" minDetail="month"
                minDate={minDate.current} maxDate={maxDate.current}
                prev2Label={null} next2Label={null} activeStartDate={null}
                tileDisabled={(date) => disableCalendarDays(date)}
                tileClassName={(date) => markCalendarDays(date)}
                onClickDay={(date) => redirectAppointmentDetails(date)}
            />
        );
    };*/

    const displayAppointments = () => {
        
    }

    return (
        <React.Fragment>
            <Navbar user={props.user} customer />

            <BackNav />
            <ResourceList user={props.user} redirectUrl="/customer/appointment/add/select-date" callback={displayAppointments}/>
        </React.Fragment>
    );
}

export default AddAppointment;
