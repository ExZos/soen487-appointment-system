import {useEffect, useRef, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {CircularProgress, makeStyles} from '@material-ui/core';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

import {api, server} from '../../endpoints/server';
import {dateFormatter} from '../../utilities/dateUtils';

// USED FOR TEMPORARY HARDCODING
const appts = [
    {
        appointmentId: 1,
        date: '2021-03-13',
        status: 'OPEN'
    }, {
        appointmentId: 2,
        date: '2021-03-21',
        status: 'CLOSED'
    }, {
        appointmentId: 3,
        date: '2021-03-29',
        status: 'OPEN'
    }
];

const useStyles = makeStyles({
    apptCalendar: {
        margin: 'auto'
    }
});

function AdminAppointmentList(props) {
    const classes = useStyles();

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
            server.get(api.listResourceAppointments)
                .then(res => {
                    setAppointments(res.data);
                    apptDict.current = res.data.reduce((a,x) => ({...a, [x.date]: x.appointmentId}), {});
                })
                .catch(() => {
                    setAppointments(appts);
                    apptDict.current = appts.reduce((a,x) => ({...a, [x.date]: x.appointmentId}), {});
                }) // TEMPORARY HARDCODING
                .finally(() => setIsLoaded(true));
        };

        minDate.current.setDate(minDate.current.getDate() + 1);
        maxDate.current.setDate(maxDate.current.getDate() + 31);

        getAppointmentList();
    }, [])

    const redirectAppointmentDetails = (date) => {
        // TODO: move this and mark calendar tiles
        // const bookedDates = appointments
        //     .filter(a => a.status === 'CLOSED')
        //     .map(a => a.date);
        // console.log(bookedDates);

        const appointmentId = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date)];
        if(!appointmentId) {
            alert("No appointment on this day");
            return;
        }

        history.push({
            pathname: '/admin/appointment/' + appointmentId
        });
    };

    const disableCalendarWeekends = (date) => {
        // Disable weekends
        const day = date.date.getDay();
        if(day === 0 || day === 6)
            return true;

        // Disable days that don't have appointments
        const dictValue = apptDict.current[dateFormatter.hyphenatedYearMonthDay(date.date)];
        return !Boolean(dictValue);
    };

    const renderAppointmentList = () => {
        if(!isLoaded)
            return <CircularProgress />;
        else if(!appointments)
            return "An error occurred while getting the appointments";

        return (
            <Calendar
                className={classes.apptCalendar}
                calendarType="US"
                minDate={minDate.current} maxDate={maxDate.current}
                tileDisabled={(date) => disableCalendarWeekends(date)}
                onClickDay={(date) => redirectAppointmentDetails(date)}
            />
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
