import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router';
import {
    CircularProgress,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, withStyles,
} from '@material-ui/core';

import {api, server} from '../../endpoints/server';
import {dateFormatter} from '../../utilities/dateUtils';

const CustomPaper = withStyles({
    root: {
        width: 'fit-content',
        margin: 'auto'
    }
})(Paper);

const CustomTableHead = withStyles((theme) => ({
    root: {
        backgroundColor: theme.palette.action.focus
    }
}))(TableHead);


function AppointmentDetails(props) {
    const params = useParams();

    const [appointment, setAppointment] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        const getAppointmentDetails = () => {
            const config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'username': props.user.username,
                    'x-api-key': props.user.token
                }
            }

            server.get(api.getAppointmentDetails + '/' + params.id, config)
                .then(res => setAppointment(res.data))
                .catch(() => setAppointment(null))
                .finally(() => setIsLoaded(true));
        };

        getAppointmentDetails();
    }, [props.user, params.id]);

    const renderAppointmentDetails = () => {
        if(!isLoaded)
            return <CircularProgress />
        else if(!appointment)
            return "This appointment doesn't exist";

        return (
          <React.Fragment>
              <TableContainer component={CustomPaper}>
                  <Table size="small">
                      <CustomTableHead>
                          <TableRow>
                              <TableCell align="center" colSpan="2"><b>BOOKED INFO</b></TableCell>
                          </TableRow>
                      </CustomTableHead>

                      <TableBody>
                          <TableRow>
                              <TableCell align="right"><b>Resource</b></TableCell>
                              <TableCell align="left">{appointment.resourceName}</TableCell>
                          </TableRow>

                          <TableRow>
                              <TableCell align="right"><b>Date</b></TableCell>
                              <TableCell align="left">{dateFormatter.prettyString(new Date(appointment.date))}</TableCell>
                          </TableRow>

                          <TableRow>
                              <TableCell align="right"><b>Status</b></TableCell>
                              <TableCell align="left">{appointment.status}</TableCell>
                          </TableRow>
                      </TableBody>

                      <CustomTableHead>
                          <TableRow>
                              <TableCell align="center" colSpan="2"><b>CLIENT INFO</b></TableCell>
                          </TableRow>
                      </CustomTableHead>

                      <TableBody>
                          <TableRow>
                              <TableCell align="right"><b>Email</b></TableCell>
                              <TableCell align="left">{appointment.email ? appointment.email : 'N/A'}</TableCell>
                          </TableRow>

                          <TableRow>
                              <TableCell align="right"><b>Message</b></TableCell>
                              <TableCell align="left">{appointment.message ? appointment.message : 'N/A'}</TableCell>
                          </TableRow>
                      </TableBody>
                  </Table>
              </TableContainer>
          </React.Fragment>
        );
    };

    return (
        <div id="appointmentDetails" className="text-center">
            <h3>Appointment Details</h3>
            <div><i>#{params.id}</i></div>

            <div className="mt-3">
                {renderAppointmentDetails()}
            </div>
        </div>
    );
}

export default AppointmentDetails;
