'use strict'
const electron = require('electron')
// const session = require('electron').session;
import {
    app,
    protocol,
    BrowserWindow,
    Menu,
    session
} from 'electron'
import {
    createProtocol,
    /* installVueDevtools */
} from 'vue-cli-plugin-electron-builder/lib'
const isDevelopment = process.env.NODE_ENV !== 'production'

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let win

let template = [{
    label: '关于',
    submenu: [{
        label: 'about...',
        click: function(item, focusedWindow) {
            if (focusedWindow) {
                const options = {
                    type: 'info',
                    title: 'about',
                    buttons: ['OK'],
                    message: 'xx题目系统，祝您使用愉快'
                }
                electron.dialog.showMessageBox(focusedWindow, options, function() {})
            }
        }
    }]
}]

// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([{
    scheme: 'app',
    privileges: {
        secure: true,
        standard: true
    }
}])

function createWindow() {
    // Create the browser window.
    win = new BrowserWindow({
        width: 800,
        height: 600,
        minWidth:300,
        minHeight:350,
        webPreferences: {
            nodeIntegration: true,
            webSecurity: false,
        }
    })

    // let mainSession = win.webContents.session;
    // mainSession.cookies.set({url:"http://test.su29029.xyz", name:"cookie1", value:"cookie_value",domain:"http://test.su29029.xyz"}, err => {
    //     console.log("cookies:");
    //     mainSession.cookies.get({}, (err, cookies) => {
    //         console.log(cookies);
    //     })
    // })

    if (process.env.WEBPACK_DEV_SERVER_URL) {
        // Load the url of the dev server if in development mode
        win.loadURL('http://test.su29029.xyz/index')
        if (!process.env.IS_TEST) win.webContents.openDevTools()
    } else {
        createProtocol('http')
            // Load the index.html when not in development
        win.webContents.openDevTools()
        win.loadURL('http://test.su29029.xyz/index')
    }

    win.on('closed', () => {
        win = null
    })
}

// Quit when all windows are closed.
app.on('window-all-closed', () => {
    // On macOS it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

app.on('activate', () => {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (win === null) {
        createWindow()
    }
})

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', async() => {
    if (isDevelopment && !process.env.IS_TEST) {
        // Install Vue Devtools
        // Devtools extensions are broken in Electron 6.0.0 and greater
        // See https://github.com/nklayman/vue-cli-plugin-electron-builder/issues/378 for more info
        // Electron will not launch with Devtools extensions installed on Windows 10 with dark mode
        // If you are not using Windows 10 dark mode, you may uncomment these lines
        // In addition, if the linked issue is closed, you can upgrade electron and uncomment these lines
        // try {
        //   await installVueDevtools()
        // } catch (e) {
        //   console.error('Vue Devtools failed to install:', e.toString())
        // }

    }
    createWindow()
    const menu = Menu.buildFromTemplate(template)
    Menu.setApplicationMenu(menu)
})

// Exit cleanly on request from parent process in development mode.
if (isDevelopment) {
    if (process.platform === 'win32') {
        process.on('message', data => {
            if (data === 'graceful-exit') {
                app.quit()
            }
        })
    } else {
        process.on('SIGTERM', () => {
            app.quit()
        })
    }
}
