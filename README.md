# AutoLogin - 自动登录模组

[![Fabric](https://img.shields.io/badge/Fabric-1.20.1-1976d2)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

一个简单的 Fabric 客户端模组，用于自动检测服务器登录提示并执行 `/login` 命令。

## 功能特性

- 自动检测聊天消息中的登录请求（支持自定义正则匹配）
- 自动发送 `/login <密码>` 命令
- 支持为不同服务器设置独立的密码
- 使用 **Cloth Config** 提供图形化配置界面（支持 [ModMenu](https://modrinth.com/mod/modmenu)）
- 提供 `/autologin` 命令用于快捷管理
- 登录成功后自动停止尝试

## 安装

1. 安装 [Fabric Loader](https://fabricmc.net/use/) 和 [Fabric API](https://modrinth.com/mod/fabric-api)
2. 安装 [Cloth Config API](https://modrinth.com/mod/cloth-config)（必需）
3. （可选）安装 [ModMenu](https://modrinth.com/mod/modmenu) 以便使用配置界面
4. 将本模组的 JAR 文件放入 `.minecraft/mods/` 文件夹
5. 启动游戏

## 使用方法

### 基本流程

1. 进入需要登录的服务器
2. 使用 `/autologin set <密码>` 设置当前服务器的密码（只需设置一次）
3. 当服务器发送登录提示时，模组会自动执行 `/login <密码>`，若当前服务器尚未特别设置密码，则使用默认密码

### 命令列表

| 命令 | 说明 |
|------|------|
| `/autologin set <密码>` | 为当前连接的服务器设置密码 |
| `/autologin reload` | 重新加载配置文件 |
| `/autologin status` | 查看当前状态和当前服务器密码是否已设置 |

### 主要配置项

| 配置项 | 说明 |
|--------|------|
| `enabled` | 是否启用自动登录功能 |
| `defaultPassword` | 默认密码（当服务器没有单独设置时使用） |
| `serverPasswords` | 服务器特定密码列表（地址 → 密码） |
| `triggerPattern` | 用于检测登录请求的正则表达式 |
| `successPattern` | 用于检测登录成功的正则表达式 |

### 图形化配置（ModMenu）

如果安装了 ModMenu，可以在游戏主菜单点击 `模组` 按钮 → 找到 **AutoLogin** → 点击配置按钮进行设置。

## 🛠 常见问题

**Q: 模组没有自动登录？**  
A: 请检查以下几点：
- 确保配置中 `enabled` 为 `true`
- 确认已为当前服务器设置了密码（使用 `/autologin status` 查看）
- 检查 `triggerPattern` 是否匹配服务器的登录提示消息
- 查看日志中是否有 `AutoLogin` 相关的错误信息

**Q: 如何适配非标准登录命令（如 `/l` 或 `/register`）？**  
A: 模组目前固定发送 `/login` 命令。如需修改，可在下一个版本中添加命令模板配置项，或自行修改源码中的 `sendCommand("login " + password)` 部分。

**Q: 密码以明文存储是否安全？**  
A: 密码以明文保存在本地配置文件中。请确保你的电脑环境安全，不要分享配置文件。未来版本可能加入简单加密。

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

> 如有问题或建议，欢迎提交 `Issue` 或 `Pull Request`