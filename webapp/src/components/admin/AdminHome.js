import React from 'react';
import {useEffect, useState} from 'react';
import {useHistory} from 'react-router';
import {Button, FormControl, InputLabel, Select, withStyles} from '@material-ui/core';

import {api, server} from '../../endpoints/server';
import Navbar from '../subcomponents/Navbar';
import LogoutButton from '../subcomponents/LogoutButton';

const ResourceSelect = withStyles({
    root: {
        minWidth: "70px"
    }
})(Select);

function AdminHome(props) {
    const history = useHistory();

    const [resources, setResources] = useState([]);
    const [selectedResource, setSelectedResource] = useState('');
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        const getResourceList = () => {
            const config = {
                headers: {
                    'username': props.user.username,
                    'x-api-key': props.user.token
                }
            };

            server.get(api.listResources, config)
                .then(res => setResources(res.data))
                .catch(() => setResources(null))
                .finally(() => setIsLoaded(true));
        };

        getResourceList();
    }, [props.user.username, props.user.token]);

    const addResourceRedirect = () => {
        history.push('/admin/resource/add');
    };

    const listAppointmentsRedirect = () => {
        if(selectedResource)
            history.push({
                pathname: '/admin/appointment',
                state: {
                    resource: selectedResource
                }
            });
    };

    const renderResourceOptions = () => {
        if(!isLoaded)
            return;
        else if(!resources)
            return <option disabled>Error</option>
        else if(resources.length === 0)
            return <option disabled>None</option>

        return resources.map((resource, i) => (
            <option key={i} value={JSON.stringify(resource)}>
                {resource.name}
            </option>
        ));
    };

    return (
        <React.Fragment>
            <Navbar user={props.user} admin />

            <div id="adminHome" className="text-center">
                <h3>View Appointments</h3>

                <div className="mt-3">
                    <FormControl variant="outlined" size="small">
                        <InputLabel id="resource-select-label">Resource</InputLabel>
                        <ResourceSelect label="Resource" labelId="resource-select-label" value={selectedResource}
                                onChange={(e) => setSelectedResource(e.currentTarget.value)} native>
                            <option disabled />

                            {renderResourceOptions()}
                        </ResourceSelect>
                    </FormControl>

                    <Button height="50" variant="contained" color="primary" disabled={selectedResource === ""} onClick={listAppointmentsRedirect}>Go</Button>
                </div>
            </div>
        </React.Fragment>
    );
}

export default AdminHome;
