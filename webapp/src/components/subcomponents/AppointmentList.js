import React, {useEffect, useState} from 'react';
import {CircularProgress, makeStyles} from '@material-ui/core';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

import {api, server} from '../../endpoints/server';
import {dateFormatter} from '../../utilities/dateUtils';

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

    const [appointments, setAppointments] = useState([]);
    const [apptDict, setApptDict] = useState({});
    const [isLoaded, setIsLoaded] = useState(false);
    const [noResourceError, setNoResourceError] = useState(false);

    useEffect(() => {
        if (props.resourceId != ''){
            setNoResourceError(false);

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
                        let newAppts = res.data.reduce((a,x) => ({...a, [x.date]: {
                            id: x.appointmentId,
                            status: x.status
                        }}), {});
                        setApptDict(newAppts);
                    })
                    .catch(() => setAppointments(null))
                    .finally(() => setIsLoaded(true));
            };
            getAppointmentList();
        }else{
            setNoResourceError(true);
        }
    }, [props.resourceId]);

    const redirectAppointmentDetails = (date) => {
        const appointment = apptDict[dateFormatter.hyphenatedYearMonthDay(date)];

        if (props.onSelectAppointmentCallBack){
            props.onSelectAppointmentCallBack(appointment.id);
        }
    };

    const disableCalendarDays = (date) => {
        // Disable days that don't have appointment
        const appointment = apptDict[dateFormatter.hyphenatedYearMonthDay(date.date)];
        return !Boolean(appointment);
    };

    const markCalendarDays = (date) => {
        const appointment = apptDict[dateFormatter.hyphenatedYearMonthDay(date.date)];

        if(!appointment)
            return;
        else if(appointment.status === 'OPEN')
            return classes.openAppt;

        return classes.closedAppt;
    };

    const renderAppointmentList = () => {
        if (noResourceError)
            return "Please select a resource";
        else if(!isLoaded)
            return <CircularProgress />;
        else if(!appointments)
            return "An error occurred while getting the appointments";

        return (
            <Calendar
                className={classes.apptCalendar}
                calendarType="US" minDetail="month"
                minDate={props.minDate} maxDate={props.maxDate}
                prev2Label={null} next2Label={null} activeStartDate={null}
                tileDisabled={(date) => disableCalendarDays(date)}
                tileClassName={(date) => markCalendarDays(date)}
                onClickDay={(date) => redirectAppointmentDetails(date)}
                defaultValue={props.selectedDate}
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
