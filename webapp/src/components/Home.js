import {useEffect} from 'react';
import LogoutButton from './subcomponents/LogoutButton';

function Home(props) {
    useEffect(() => {
        console.log(props?.user);
    }, [props]);

    return (
        <div id="home">
            <h3>Home</h3>

            {/*Feel free to change anything here, just used to test login*/}
            {props?.user?.token}
            <LogoutButton username={props.user?.username} email={props.user?.email} token={props.user?.token} />
        </div>
    );
}

export default Home;
