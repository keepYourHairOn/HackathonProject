import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import '../../styles/Wire/Wire.css';

class Wire extends Component {
    constructor (props) {
        super(props);
        this.state = {
            styles: {
                background: Constants.layerColor[this.props.layer],
                opacity: 0.5,
                gridColumn: 'col ' + this.props.column,
                gridRow: 'row ' + this.props.row,
                gridTemplateRows: '5px',
                gridColumnGap: '0'
            }
        }
    }

    render() {
        return (
            <div className="wire" style={this.state.styles}>
            </div>
        );
    }
}

export default Wire;