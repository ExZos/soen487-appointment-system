import {useEffect} from 'react';

function Home(props) {
    useEffect(() => {
        console.log(props?.user);
    }, [props]);

    return (
        <div id="home">
            <h3>Home</h3>

            {props?.user?.token}
        </div>
    );
}

export default Home;
