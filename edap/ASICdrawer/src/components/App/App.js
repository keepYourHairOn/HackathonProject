import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import Grid from "../Grid/Grid.js";
import Legend from '../Legend/Legend.js';
import '../../styles/App/App.css';

class App extends Component {
    constructor() {
        super();
        this.state = {
            layers: 0,
        }
    }

    componentDidMount() {
        fetch('http://localhost:8080/result')
            .then(data => data.json())
            .then((data) => {
                console.log("fhf")
                this.setState({ data: data, layers: data.length })
            });
    }

    render() {
        if (!this.state.data) {
            return (<div>Wait for response</div>);
        }
        return (
            <div>
                <Grid grid={this.state.data}/>
                <Legend layers={this.state.layers}/>
            </div>
        );
    }
}

export default App;