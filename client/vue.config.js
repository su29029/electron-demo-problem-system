module.exports = {
  pluginOptions: {
    electronBuilder:{
      builderOptions:{
        "productName":"xx题目系统",
        "appId": "xyz.su29029.test",
        "copyright":"su29029",
        "directories": { 
          "output": "electron_build"
        }, 
        "nsis": {
          "oneClick": false, 
          "allowElevation": true, 
          "allowToChangeInstallationDirectory": true,
          "createDesktopShortcut": true, 
          "createStartMenuShortcut": true
        },
        "publish": [
          {
            "provider": "generic",
            "url": "http://test.su29029.xyz/" 
          }
        ],
        "win": {
          "target": [
            {
              "target": "nsis",
              "arch": [
                "x64"
              ]
            }
          ]
        },
        "linux": {
        }
      }
    }
  }
}


