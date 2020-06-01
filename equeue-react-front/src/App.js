import React, { Component } from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import './App.css';
import FilesUploadComponent from './components/FilesUploadComponent';
import FileUploadComponent from './components/FileUploadComponent';
import AvatarUploadComponent from './components/AvatarUploadComponent';

class App extends Component {
  render() {
    return (
      <div className="App">
        <br />
        <AvatarUploadComponent />
        <br />
        <hr />
        <FileUploadComponent />
        <br />
        <hr />
        <FilesUploadComponent />
      </div>
    );
  }
}

export default App;