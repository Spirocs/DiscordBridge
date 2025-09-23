# DiscordBridge

**Status:** 🚧 Work in progress

A bridge between services (Discord + others) to forward messages, events or notifications in a customizable way.  

---

## 🚀 Problem & Concept

There are many cases where you want messages or events from one platform (e.g. Discord) mirrored or forwarded to another channel, service or user. But often:

- Bots are too generic  
- Setup is complex  
- No fine-grained control over who sees what  

**DiscordBridge** aims to offer:

- Customizable forwarding rules  
- Simple setup (once core is up)  
- Good control over what gets forwarded and how  

---

## 🛠 Features (Current & Planned)

| Feature | Status |
|---------|--------|
| Basic message forwarding from Discord to target | ✓ (in development) |
| Configurable channels / targets | ⚙️ Planned |
| Filter rules (e.g. user, keyword) | ⚙️ Planned |
| Logging / history of forwarded messages | ⚙️ Planned |
| Error handling, retries, failure feedback | ⚙️ Planned |

---

## ⚙️ Tech Stack & Architecture

- Language: **Java**  
- Build tool: **Gradle**  
- Structure: modular (core forwarding logic, configuration, possibly plugin‐like filters)  
- Runtime assumptions: runs as a bot or service connected to Discord via API  

---

## 📋 Setup / Local Development

Here is how you might get started locally:

```bash
git clone https://github.com/Spirocs/DiscordBridge.git
cd DiscordBridge
./gradlew build
```

Then:

- Configure service / bot credentials (Discord token, etc.)  
- Define forwarding rules via config files or a setup UI (once available)  
- Run the service

---

## ⚠️ Known Limitations & Next Steps

- Not all forwarding/filtering features implemented yet  
- No UI for configuration yet – everything done via config files or code  
- Error handling and logging still basic  
- Need tests, better documentation, and deployment script  

---

## ✨ What Makes It Interesting

- Potentially very useful for communities, servers, or serious Discord power users  
- If polished, could become a product/service that many smaller Discord admins would pay for or use  
- Good showcase of backend skills, API integration, and real-world problem solving  

---

## 📞 Contact & Collaboration

Even though the project isn’t finished, I’m open to feedback, ideas or collaboration.  
➡️ [GitHub Profile](https://github.com/Spirocs)  

