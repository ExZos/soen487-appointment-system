import React from 'react';
import {useEffect} from 'react';
import LogoutButton from '../subcomponents/LogoutButton';
import Navbar from '../subcomponents/Navbar';

function UserHome(props) {
    useEffect(() => {
        console.log(props?.user);
    }, [props]);

    return (
        <React.Fragment>
            <Navbar user={props.user} />
            <div id="home">
                <h3>Home</h3>

                {/*Feel free to change anything here, just used to test login*/}
                {props?.user?.token} HELOLL
                <LogoutButton username={props.user?.username} email={props.user?.email} token={props.user?.token} />
            </div>
        </React.Fragment>
    );
}

export default UserHome;
