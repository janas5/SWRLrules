import React, { Component } from 'react';
import { Button, FormGroup, FormControl, Col } from 'react-bootstrap';
import { Icon } from 'react-fa';

class Rule extends Component {
    render() {
      return (
        <div className="query-result">
          {/* {this.props.details.id.value} */}
          {this.props.details}
        </div>
      );
    }
  }

export default Rule;