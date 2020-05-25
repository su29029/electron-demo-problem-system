import Vue from 'vue'
import VueRouter from 'vue-router'
import Register from '../components/Register.vue'
import Login from '../components/Login.vue'
import Index from '../components/Index.vue'
import Profile from '../components/Profile.vue'
import Problem from '../components/Problem.vue'
import axios from 'axios'
import store from '../store/index'
Vue.use(VueRouter)

const routes = [{
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/index',
        name: 'Index',
        component: Index
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/problem',
        name: 'problem',
        component: Problem,
        beforeEnter: (to, from, next) => {
            if (store.state.user) {
                next();
            } else {
                axios.get('/isLogin').then(ret => {
                    if (ret.data.msg === "pass") {
                        store.state.user = ret.data.user.username;
                        if (sessionStorage.getItem("difficulty") &&
                            sessionStorage.getItem("language") &&
                            sessionStorage.getItem("problemNumber")) {
                            next();
                        } else {
                            next('/index');
                        }
                    } else {
                        next('/login');
                    }
                }).catch(err => {
                    console.log(err);
                    next('/login');
                })
            }
        }
    },
    {
        path: '/',
        redirect: '/index'
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router