import React from 'react';
import {Component} from 'react';
function getcontent(){
    return  'Weather is nice go outside'; 
}
function Content() {
    return (
    <div class="container">
      <h1> 
        {getcontent()}
      </h1>
    </div>
    );
  }
  export default Content