import React, {Component} from 'react';
import { genericTypeAnnotation } from '@babel/types';
let runner;
const style= {
    backgroundColor : "#99ccff",
    textAlign : 'right',
    marginRight: '5%'
}
class Clock extends Component{
    constructor(props){
        super(props);
        this.state={
            'time':this.gettime()
        };

    }
    gettime=()=>{
        const hour12=false;
        const hour='2-digit';
        const minute = '2-digit';
        const second='2-digit';
        const year = 'numeric';
        const weekday = 'long';
        const month = 'long';
        const day = '2-digit';
        return new Date().toLocaleTimeString('en-us',{'hour12': hour12, 'hour': hour, 'minute':minute,'second': second, 'year':year,'weekday':weekday,'month': month,'day':day});
    }
    componentDidMount() {
        runner = setInterval(() => {
          this.setState({time:this.gettime()});
        }, 1000);
    }
    render(){
        return (<h2 style={style}>{this.state.time}</h2>);
    }
}
export default Clock;