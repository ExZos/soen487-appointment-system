import React from 'react';
import {useEffect, useState} from 'react';
import {useHistory} from 'react-router';
import {Button, FormControl, InputLabel, Select, withStyles} from '@material-ui/core';

import {api, server} from '../../endpoints/server';
import {ArrowForward} from '@material-ui/icons';

const ResourceSelect = withStyles({
    root: {
        minWidth: '70px'
    }
})(Select);

const CustomButton = withStyles({
    root: {
        height: '40px',
        borderTopLeftRadius: 0,
        borderBottomLeftRadius: 0,
        boxShadow: 'none',
        left: '-2px'
    }
})(Button);

function ResourceList(props) {
    const [resources, setResources] = useState([]);
    const [selectedResource, setSelectedResource] = useState(props.selectedResource);
    const [isLoaded, setIsLoaded] = useState(false);

    //Initial call to get the resource list
    useEffect(() => {
        const getResourceList = () => {
            const config = {
                headers: {
                    'email': props.user.email,
                    'x-api-key': props.user.token
                }
            };

            server.get(api.listResources, config)
                .then(res => setResources(res.data))
                .catch(() => setResources(null))
                .finally(() => setIsLoaded(true));
        };

        getResourceList();
    }, [props.user.email, props.user.token]);

    //If the control itself update the selected resource 
    useEffect(() => {
        if (props.onSelectResourceCallBack){
            props.onSelectResourceCallBack(selectedResource);
        }
    }, [selectedResource]);

    //If the value passed in by the parent change - used for inital call
    useEffect(() => {
        setSelectedResource(props.selectedResource);
    }, [props.selectedResource]);

    const listAppointmentsRedirect = () => {
        if (props.onSelectResourceCallBack){
            props.onSelectResourceCallBack(selectedResource)
        }
    };

    const renderResourceOptions = () => {
        if(!isLoaded)
            return;
        else if(!resources)
            return <option disabled>Error</option>
        else if(resources.length === 0)
            return <option disabled>None</option>

        return resources.map((resource, i) => (
            <option key={i} value={resource.resourceId}>
                {resource.name}
            </option>
        ));
    };

    return (
        <React.Fragment>
            <div id="resource-select" className="text-center">
                <div style={{height: '100%'}} className="mt-3">
                    <FormControl variant="outlined" size="small">
                        <InputLabel id="resource-select-label">Resource</InputLabel>
                        <ResourceSelect label="Resource" labelId="resource-select-label" value={selectedResource}
                               onChange={(e) => setSelectedResource(e.currentTarget.value)} native>
                            <option disabled />

                            {renderResourceOptions()}
                        </ResourceSelect>
                    </FormControl>

                    <CustomButton variant="contained" color="primary" disabled={selectedResource === ""} onClick={listAppointmentsRedirect}>
                        <ArrowForward />
                    </CustomButton>
                </div>
            </div>
        </React.Fragment>
    );
}

export default ResourceList;
