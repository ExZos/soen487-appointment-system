import React, {useEffect, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {useParams} from 'react-router';
import {
    Box,
    Button,
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
import {dateConverter, dateFormatter} from '../../utilities/dateUtils';
import BackNav from '../subcomponents/BackNav';
import Navbar from '../subcomponents/Navbar';

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
    //const params = useParams();
    const history = useHistory();
    const [appointment, setAppointment] = useState(props.appointment);

    useEffect(() => {
        console.log(props.appointment)
    });

    const deleteAppointment = () => {
    }

    const updateAppointment = () => {
        history.push({
            pathname: '/customer/appointment/' + appointment.appointmentId
        });
    }

    const renderAppointmentDetails = () => {
        console.log("Inside subcomponent");

        if(!appointment)
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
                              <TableCell align="left">
                                  {dateFormatter.prettyString(dateConverter.fromSQLDateString(appointment.date))}
                              </TableCell>
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
        <React.Fragment>
            <div id="appointmentDetails" className="text-center">
                <div><i>#{appointment.appointmentId}</i></div>

                <div className="mt-3">
                    {renderAppointmentDetails()}
                </div>
                <div>
                <Box mt={2}>
                    <Button variant="contained" color="primary" onClick={updateAppointment} style={{marginRight: "5px"}}>Update</Button>
                    <Button variant="contained" color="secondary" onClick={deleteAppointment} >Delete</Button>
                </Box>
                </div>
            </div>
        </React.Fragment>
    );
}

export default AppointmentDetails;
