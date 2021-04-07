import React, {useState} from 'react';
import {Button, FormControl, TextField, withStyles} from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';

import {api, server} from '../../endpoints/server';
import {useHistory} from 'react-router';
import Navbar from '../subcomponents/Navbar';

const CustomButton = withStyles({
    root: {
        height: '40px',
        borderTopLeftRadius: 0,
        borderBottomLeftRadius: 0,
        boxShadow: 'none',
        left: '-2px'
    }
})(Button);

function AddResourceForm(props) {
    const history = useHistory();

    const [name, setName] = useState('');
    const [nameError, setNameError] = useState('');

    const submit = (callback) => {
      if(!nameInputIsValid()) {
          setNameError('Required');
          return;
      }

      callback();
    };

    const nameInputIsValid = () => {
        return name && name.trim();
    };

    const addResource = () => {
        const params = new URLSearchParams();
        params.append('name', name);

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'username': props.user.username,
                'x-api-key': props.user.token
            }
        };

        server.post(api.addResource, params, config)
            .then(() => {
                alert('SUCCESSFULLY ADDED RESOURCE');
                history.push('/admin/home');
            })
            .catch(err => {
                if(err.response)
                    alert(err.response.data);
            });
    };

    return (
        <React.Fragment>
            <Navbar user={props.user} admin />

            <div id="resourceForm" className="text-center">
                <h3>Add Resource</h3>

                <div className="mt-4">
                    <FormControl>
                        <TextField variant="outlined" size="small" type="name" label="Name" value={name} error={nameError !== ''} helperText={nameError}
                                   onChange={(e) => setName(e.currentTarget.value)} />
                    </FormControl>

                    <CustomButton variant="contained" color="primary" startIcon={<AddIcon />}
                                  disabled={!nameInputIsValid()} onClick={() => submit(addResource)}>
                        Add
                    </CustomButton>
                </div>
            </div>
        </React.Fragment>
    );
}

export default AddResourceForm;
