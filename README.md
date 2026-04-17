# AutoLogin - 自动登录模组

[![Fabric](https://img.shields.io/badge/Fabric-1.20.1-1976d2)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

一个简单的 Fabric 客户端模组，用于自动检测服务器登录提示并执行自定义登录命令。

## 功能特性

- 🔍 自动检测聊天消息中的登录请求（支持自定义正则匹配）
- 🔐 自动发送自定义登录命令（支持模板，如 `/login`、`/l`、`/register` 等）
- 🌍 支持为不同服务器设置独立的密码
- ⚙️ 使用 **Cloth Config** 提供图形化配置界面（支持 [ModMenu](https://modrinth.com/mod/modmenu)）
- 📝 提供 `/autologin` 命令用于快捷管理
- 🚀 登录成功后自动停止尝试

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
3. 当服务器发送登录提示时，模组会根据配置的命令模板自动发送登录命令（例如 `/login <密码>` 或 `/l <密码>`）

### 命令列表

| 命令 | 说明 |
|------|------|
| `/autologin set <密码>` | 为当前连接的服务器设置密码 |
| `/autologin reload` | 重新加载配置文件 |
| `/autologin status` | 查看当前状态和当前服务器密码是否已设置 |

### 主要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `enabled` | 是否启用自动登录功能 | `true` |
| `defaultPassword` | 默认密码（当服务器没有单独设置时使用） | `""` |
| `serverPasswords` | 服务器特定密码列表（地址 → 密码） | `[]` |
| `triggerPattern` | 用于检测登录请求的正则表达式 | `.*/(login\|reg\|register).*\|.*未登录.*\|.*Please login.*\|.*密码.*\|.*需要登录.*` |
| `successPattern` | 用于检测登录成功的正则表达式 | `.*登录成功.*\|.*Login successful.*\|.*已登录.*` |
| `loginCommandTemplate` | 登录命令模板，`%s` 会被密码替换 | `"login %s"` |

**关于 `loginCommandTemplate` 的说明**：
- 模板中的 `%s` 是密码占位符，会被实际密码替换。
- 例如：
    - `"login %s"` → 发送 `/login 我的密码`
    - `"l %s"` → 发送 `/l 我的密码`
    - `"register %s %s"` 需要两个密码参数，暂不支持（请保持一个 `%s`）。

### 图形化配置（ModMenu）

如果安装了 ModMenu，可以在游戏主菜单点击 `模组` 按钮 → 找到 **AutoLogin** → 点击配置按钮进行设置。

## 🛠 常见问题

**Q: 模组没有自动登录？**  
A: 请检查以下几点：
- 确保配置中 `enabled` 为 `true`
- 确认已为当前服务器设置了密码（使用 `/autologin status` 查看）
- 检查 `triggerPattern` 是否匹配服务器的登录提示消息
- 检查 `loginCommandTemplate` 是否设置正确（例如服务器需要 `/l` 命令，你却配置成了 `login`）
- 查看日志（`.minecraft/logs/latest.log`）中是否有 `AutoLogin` 相关的错误或信息

**Q: 如何适配非标准登录命令（如 `/l` 或 `/register`）？**  
A: 直接修改配置中的 `loginCommandTemplate` 即可，例如改为 `"l %s"`。

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

> 如有问题或建议，欢迎提交 `Issue` 或 `Pull Request`