import {useHistory} from 'react-router';
import ArrowBackIosRoundedIcon from '@material-ui/icons/ArrowBackIosRounded';
import {withStyles} from '@material-ui/core';

const CustomArrowBack = withStyles({
    root: {
        position: 'absolute',
        left: '2pc',

        '&:hover': {
            cursor: 'pointer'
        }
    }
})(ArrowBackIosRoundedIcon);

function BackNav(props) {
    const history = useHistory();

    return (
        <CustomArrowBack fontSize="large" onClick={history.goBack} />
    );
}

export default BackNav;
