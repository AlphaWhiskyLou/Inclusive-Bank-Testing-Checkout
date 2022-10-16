<template>
  <el-container class="home_container">
    <el-header>
      <div class="home_title">最有爱的网上银行</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link home_userinfo">
    {{currentUserName}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
  </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            <el-dropdown-item command="associate">关联账户</el-dropdown-item>
            <el-dropdown-item command="verify">验证关联</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo" style="background-color: #ECECEC" router>
          <template v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden">
            <el-submenu :index="index+''" :key="index">
              <template slot="title">
                <i :class="item.iconCls"></i>
                <span>{{item.name}}</span>
              </template>
              <el-menu-item v-for="child in item.children" v-if="!child.hidden" :index="child.path" :key="child.path">
                {{child.name}}
              </el-menu-item>
            </el-submenu>
<!--            <template v-else>-->
<!--              <el-menu-item :index="item.children[0].path">-->
<!--                <i :class="item.children[0].iconCls"></i>-->
<!--                <span slot="title">{{item.children[0].name}}</span>-->
<!--              </el-menu-item>-->
<!--            </template>-->
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main class="bkg">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
          </el-breadcrumb>
          <keep-alive>
            <router-view v-if="this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!this.$route.meta.keepAlive"></router-view>

          <el-dialog
            title="关联账户"
            :visible.sync="dialogVisibleA"
            width="30%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="130px" class="demo-ruleForm">
              <el-form-item label="老人身份证号" prop="oldname">
                <el-input v-model="ruleForm.oldname"></el-input>
              </el-form-item>
              <el-form-item label="亲属关系" prop="radio">
                <el-radio-group v-model="ruleForm.radio">
                  <el-radio label="dad">父亲</el-radio>
                  <el-radio label="mom">母亲</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">绑定</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
<!--            <span>这是一段信息</span>-->
<!--            <div slot="footer" class="dialog-footer">-->
<!--              <el-button @click="dialogVisibleA = false">取 消</el-button>-->
<!--              <el-button type="primary" @click="dialogVisibleA = false">确 定</el-button>-->
<!--            </div>-->
          </el-dialog>

          <el-dialog
            title="关联账户验证"
            :visible.sync="dialogVisibleV"
            width="30%">
            <el-button type="primary" @click="submitForm1">接受</el-button>
            <el-button type="primary" @click="submitForm2">拒绝</el-button>

          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import SockJS from 'sockjs-client';
import  Stomp from 'stompjs';
var stompClient=null;
export default {
  name: "Home",
  methods: {
        connect() {
            var that=this;
            var socket = new SockJS('http://101.132.250.23:81/endPoint');
            stompClient = Stomp.over(socket);//2创建STOMP协议的webSocket客户端。
            stompClient.connect({}, function (frame) {//3连接webSocket的服务端。
                console.log('开始进行连接Connected: ' + frame);
                stompClient.subscribe('/user/' + that.currentUserName + '/msg', function (response) {
                    that.$alert(response.body);
                });
            });
        },
        disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        },
        sendMessage() {
          var message="您的儿子/女儿"+this.currentUserName+'请求关联您的账号';
          var userName= this.ruleForm.oldname;
          stompClient.send("/ws/sendMessage", { token: 1 },JSON.stringify({ 'userName': userName,'message':message}))
        },
        submitForm(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.sendMessage();
              this.$axios
                .get("http://101.132.250.23:81/user/unity/"+this.currentUserName+"/"+this.ruleForm.oldname+"/"+this.ruleForm.radio)
                .then(response => {
                  if (response.data.code === 200) {
                   this.$alert('绑定成功！');
                    console.log('注册成功');
                  }
              else {
                this.$alert('绑定失败！');
                console.log('绑定失败');
                return false;
              }
            })
        } else {
          console.log('绑定失败');
          return false;
        }
      });
    },
    submitForm1(formName) {
          this.$axios
            .get("http://101.132.250.23:81/user/unityverify/"+this.currentUserName)//localStorage.getItem('token'))
            .then(response => {
              if (response.data.code === 200) {
                this.$alert('认证成功！');
                console.log('认证成功');
              }
              else {
                this.$alert('认证失败！');
                console.log('认证失败');
                return false;
              }
              this.dialogVisibleA = false
            })
    },
    submitForm2(formName) {
      this.$axios
        .get("http://101.132.250.23:81/user/deleteverify/"+this.currentUserName)//localStorage.getItem('token'))
        .then(response => {
          if (response.data.code === 200) {
            this.$alert('拒绝成功！');
            console.log('拒绝成功');
          }
          else {
            this.$alert('拒绝失败！');
            console.log('拒绝失败');
            return false;
          }
          this.dialogVisibleA = false
        })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleCommand(command) {
      if(command==="logout") {
        localStorage.removeItem('token');
        window.location.reload();
      }
      else if (command==="associate") {
        this.dialogVisibleA = true;
      }
      else if (command==="verify") {
        this.dialogVisibleV = true;
      }
    },
    getUserName() {
      this.$axios({
        method:"get",
        url:"http://101.132.250.23:81/user/getUser",
        headers:{
          "token": localStorage.getItem("token")
        }
      })
      .then(response=>{
        this.currentUserName = response.data.data.userName;
      })
    }
  },data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    return {
      currentUserName: '',
      dialogVisibleA: false,
      dialogVisibleV: false,
      ruleForm: {
        oldname: '',
        radio:''
      },
      rules: {
        oldname: [
          { required: true, message: '请输入您亲属的身份证', trigger: 'blur' },
          {pattern:/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入合法身份证号！', trigger: 'blur'}
        ],
        radio:[
          {required: true, message:'请选择您亲属的身份'},
        ]
      },
    }
  },
   created(){
     this.getUserName();
    },
    watch:{
        currentUserName(){
            this.$nextTick(()=>{
               this.connect();//websocket建立连接
            })
        }
    }
}
</script>



<style scoped>
.bkg{
  width: 100%;
  height: 100%;
  background-color: cornflowerblue;
}
.home_container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
  background-color: cornflowerblue;
}

.el-header {
  background-color: #20a0ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-aside {
  background-color: #ECECEC;
}

.el-main {
  background-color: #fff7e8;
  color: #000;
  text-align: center;
}

.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}
</style>
