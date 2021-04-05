import {useState} from 'react';
import {Button, FormControl, TextField} from '@material-ui/core';

import {api, server} from '../../endpoints/server';
import {useHistory} from 'react-router';

function AddResourceForm(props) {
    const history = useHistory();

    const [name, setName] = useState('');
    const [nameError, setNameError] = useState('');

    const submit = (callback) => {
      if(!name || !name.trim()) {
          setNameError('Required');
          return;
      }

      callback();
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
        <div id="resourceForm" className="text-center">
            <h3>Add Resource</h3>

            <div className="mt-4">
                <FormControl>
                    <TextField variant="outlined" size="small" type="name" label="Name" value={name} error={nameError !== ''} helperText={nameError}
                               onChange={(e) => setName(e.currentTarget.value)} />
                </FormControl>

                <Button variant="contained" color="primary" onClick={() => submit(addResource)}>Add</Button>
            </div>
        </div>
    );
}

export default AddResourceForm;
