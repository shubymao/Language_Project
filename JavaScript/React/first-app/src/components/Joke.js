import React from 'react'
import placeholder from '../img/placeholder.jpg'
function defined(item){
    if(item===undefined)return false;
    return true;
}
function Joke(props){
    console.log(props);
    return (

        <div className="card">
            <div className="card-container">
                <h4>{props.props.question}</h4>
                <p>{props.props.line}</p>
            </div>
        </div>
    );
}
export default Joke
