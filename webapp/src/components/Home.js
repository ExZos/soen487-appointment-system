import {useLocation} from 'react-router';

function Home() {
    const location = useLocation();

    return (
        <div id="home">
            {location.state.token}
        </div>
    );
}

export default Home;
