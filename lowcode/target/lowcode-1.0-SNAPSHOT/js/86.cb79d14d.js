"use strict";(self["webpackChunklowcode"]=self["webpackChunklowcode"]||[]).push([[86],{5425:function(e,t,o){o.d(t,{U:function(){return a}});var l=o(1020),n=o(4870);const a=(0,l.Q_)("httpUrl",(()=>{const e=(0,n.iH)("http://124.71.232.195:8080/lowcode/#");return{url:e}}))},710:function(e,t,o){o.d(t,{Z:function(){return p}});var l=o(3396),n=o(7139);const a={class:"item-title"};var u={__name:"FormGenerator",props:["element"],setup(e){return(t,o)=>{const u=(0,l.up)("a-input"),s=(0,l.up)("a-textarea"),i=(0,l.up)("a-radio"),p=(0,l.up)("a-radio-group"),r=(0,l.up)("a-select"),m=(0,l.up)("a-rate"),c=(0,l.up)("a-date-picker"),d=(0,l.up)("a-typography-text"),v=(0,l.up)("a-typography-paragraph"),w=(0,l.up)("a-divider");return(0,l.wg)(),(0,l.iD)(l.HY,null,[(0,l._)("div",a,(0,n.zw)(e.element.name),1),"input"==e.element.options.type?((0,l.wg)(),(0,l.j4)(u,{key:0,value:e.element.options.value,"onUpdate:value":o[0]||(o[0]=t=>e.element.options.value=t),allowClear:e.element.options.allowClear,bordered:e.element.options.bordered,placeholder:e.element.options.placeholder,showCount:e.element.options.showCount,onBlur:o[1]||(o[1]=o=>e.element.refill?t.$emit("refill"):null)},null,8,["value","allowClear","bordered","placeholder","showCount"])):"textarea"==e.element.options.type?((0,l.wg)(),(0,l.j4)(s,{key:1,value:e.element.options.value,"onUpdate:value":o[2]||(o[2]=t=>e.element.options.value=t),allowClear:e.element.options.allowClear,autoSize:e.element.options.autoSize,placeholder:e.element.options.placeholder,showCount:e.element.options.showCount},null,8,["value","allowClear","autoSize","placeholder","showCount"])):"radio"==e.element.options.type?((0,l.wg)(),(0,l.j4)(p,{key:2,value:e.element.options.value,"onUpdate:value":o[3]||(o[3]=t=>e.element.options.value=t),name:"radioGroup"},{default:(0,l.w5)((()=>[((0,l.wg)(!0),(0,l.iD)(l.HY,null,(0,l.Ko)(e.element.options.valueData,((e,t)=>((0,l.wg)(),(0,l.j4)(i,{key:t,value:e.value},{default:(0,l.w5)((()=>[(0,l.Uk)((0,n.zw)(e.value),1)])),_:2},1032,["value"])))),128))])),_:1},8,["value"])):"select"==e.element.options.type?((0,l.wg)(),(0,l.j4)(r,{key:3,value:e.element.options.value,"onUpdate:value":o[4]||(o[4]=t=>e.element.options.value=t),style:{width:"250px"},options:e.element.options.valueData},null,8,["value","options"])):"rate"==e.element.options.type?((0,l.wg)(),(0,l.j4)(m,{key:4,value:e.element.options.value,"onUpdate:value":o[5]||(o[5]=t=>e.element.options.value=t),style:{"background-color":"rgb(255, 255, 255)"},count:e.element.options.limit},null,8,["value","count"])):"datePicker"==e.element.options.type?((0,l.wg)(),(0,l.j4)(c,{key:5,value:e.element.options.value,"onUpdate:value":o[6]||(o[6]=t=>e.element.options.value=t),format:"YYYY-MM-DD",valueFormat:"YYYY-MM-DD"},null,8,["value"])):"text"==e.element.options.type?((0,l.wg)(),(0,l.j4)(v,{key:6,style:(0,n.j5)({"font-family":e.element.options.fontFamily,"font-size":e.element.options.fontSize+"px"})},{default:(0,l.w5)((()=>[(0,l.Wm)(d,{type:e.element.options.textType},{default:(0,l.w5)((()=>[(0,l.Uk)((0,n.zw)(e.element.options.value),1)])),_:1},8,["type"])])),_:1},8,["style"])):"divider"==e.element.options.type?((0,l.wg)(),(0,l.j4)(w,{key:7,style:(0,n.j5)(e.element.options.style),dashed:e.element.options.dashed,orientation:e.element.options.orientation},{default:(0,l.w5)((()=>[(0,l.Uk)((0,n.zw)(e.element.options.value),1)])),_:1},8,["style","dashed","orientation"])):(0,l.kq)("",!0)],64)}}},s=o(89);const i=(0,s.Z)(u,[["__scopeId","data-v-6dc9a7b8"]]);var p=i},5086:function(e,t,o){o.r(t),o.d(t,{default:function(){return v}});o(7658);var l=o(3396),n=o(7139),a=o(4870),u=o(710),s=o(4161),i=o(2483),p=o(5425);const r={class:"form-title"};var m={__name:"PublicSubmission",setup(e){const t=(0,p.U)(),o=(0,i.tv)(),m=(0,i.yj)(),c=(0,a.iH)(""),d=(0,a.iH)({title:"表单标题",content:[]});function v(){const e=[];for(let t of d.value.content)"text"!=t.options.type&&"divider"!=t.options.type&&e.push({schema:t.schema,field:t.field,value:t.options.value});return e}function w(){const e=v();s.Z.post("retrace.do",{content:e}).then((e=>{const t=e.data.length,o=e.data;for(let l=0;l<t;l++)d.value.content[l].options.value=o[l].value})).catch((e=>{console.log(e)}))}function y(){const e=v(),t=JSON.stringify(e);s.Z.post("saveData.do",{url:c,submission:t}).then((e=>{o.push({name:"submissionFinish"})})).catch((e=>{console.log(e)}))}return(0,l.wF)((()=>{c.value=t.url+"/user/submission/?subUrl="+m?.query?.subUrl,s.Z.get("formData.do",{params:{url:c.value}}).then((e=>{d.value=e.data,""===e.data&&o.push({name:"notFound"})})).catch((e=>{console.log(e)}))})),(e,t)=>{const o=(0,l.up)("a-layout-sider"),a=(0,l.up)("a-divider"),s=(0,l.up)("a-form-item"),i=(0,l.up)("a-form"),p=(0,l.up)("a-button"),m=(0,l.up)("a-layout");return(0,l.wg)(),(0,l.j4)(m,{class:"container"},{default:(0,l.w5)((()=>[(0,l.Wm)(o,{theme:"light",class:"main-left"}),(0,l.Wm)(m,{class:"content"},{default:(0,l.w5)((()=>[(0,l.Wm)(i,null,{default:(0,l.w5)((()=>[(0,l._)("div",r,(0,n.zw)(d.value.title),1),(0,l.Wm)(a),((0,l.wg)(!0),(0,l.iD)(l.HY,null,(0,l.Ko)(d.value.content,((e,t)=>((0,l.wg)(),(0,l.j4)(s,{key:t,class:"item-content"},{default:(0,l.w5)((()=>[(0,l.Wm)(u.Z,{element:e,onRefill:w},null,8,["element"])])),_:2},1024)))),128))])),_:1}),(0,l.Wm)(p,{type:"primary",class:"submit-button",onClick:y},{default:(0,l.w5)((()=>[(0,l.Uk)(" 提交 ")])),_:1})])),_:1}),(0,l.Wm)(o,{theme:"light",class:"main-right"})])),_:1})}}},c=o(89);const d=(0,c.Z)(m,[["__scopeId","data-v-45215a64"]]);var v=d}}]);
//# sourceMappingURL=86.cb79d14d.js.map