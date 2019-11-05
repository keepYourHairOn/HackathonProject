import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import '../../styles/LegendItem/LegendItem.css';

class LegendItem extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: Constants.cellTypeCodes[this.props.type] + (this.props.layer ? " layer " + this.props.layer : ""),
            styles: {
                background: this.props.color,
                opacity: this.props.type != 1 ? 1: 0.6,
                float: 'left'
            }
        }
    }

    render() {
        return (
            <div className="legendItemWrap">
                <div className="legendItem" style={this.state.styles}>
                </div>
                <div className="legendName">
                    {this.state.name}
                </div>
            </div>
        );
    }
}

export default LegendItem;