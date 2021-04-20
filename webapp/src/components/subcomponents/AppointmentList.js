import React, {useEffect, useRef, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {CircularProgress, makeStyles} from '@material-ui/core';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

import {api, server} from '../../endpoints/server';
import {dateFormatter} from '../../utilities/dateUtils';
import Navbar from '../subcomponents/Navbar';
import BackNav from '../subcomponents/BackNav';

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

function AppointmentList(props) {
    const classes = useStyles();
    const history = useHistory();

    const [appointments, setAppointments] = useState([]);
    const apptDict = useRef({});
    const [isLoaded, setIsLoaded] = useState(false);

    const minDate = useRef(new Date());
    const maxDate = useRef(new Date());

    useEffect(() => {
        const getAppointmentList = () => {
            const config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'email': props.user.email,
                    'x-api-key': props.user.token
                }
            }

            server.get(api.listOpenAppointments + '/' + props.resourceId, config)
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
    }, [history, props.resource]);

    const redirectAppointmentDetails = (date) => {
        const appointment = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date)];
        console.log(appointment);

        if (props.onSelectAppointmentCallBack){
            props.onSelectAppointmentCallBack(appointment.id);
        }
        /*if(!appointment) {
            alert("No appointment on this day");
            return;
        }

        history.push({
            pathname: '/admin/appointment/' + appointment.id
        });*/
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
                selectedDate={props.selectedDate}
            />
        );
    };

    return (
        <React.Fragment>
            <div id="adminAppointmentList" className="text-center">
                <div className="appointmentList mt-3">
                    {renderAppointmentList()}
                </div>
            </div>
        </React.Fragment>
    );
}

export default AppointmentList;
