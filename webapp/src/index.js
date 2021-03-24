import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Switch } from 'react-router';
import { Route } from 'react-router-dom';
import { createBrowserHistory } from 'history';
import 'bootstrap/dist/css/bootstrap.min.css';

import './styles/index.css';
import App from './components/App';

const history = createBrowserHistory();

const routing = (
    <React.StrictMode>
        <Router history={history}>
            <Switch>
                <Route path="/" component={App} />
            </Switch>
        </Router>
    </React.StrictMode>
);

ReactDOM.render(routing, document.getElementById('root'));
