// Dev-only proxies: external image hosts that don't send CORS headers are
// routed through the dev server, which is not subject to browser CORS.
// Production builds skip these and will need a backend image proxy.
console.log("[webpack.config.d] applying image proxy rules");
const UA =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 " +
    "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";
config.devServer = config.devServer || {};
config.devServer.proxy = [
    {
        context: ["/img-proxy"],
        target: "https://westwood.ie",
        changeOrigin: true,
        pathRewrite: { "^/img-proxy": "" },
        secure: true,
        followRedirects: true,
        headers: { Referer: "https://westwood.ie/", "User-Agent": UA },
    },
    {
        context: ["/ddg-proxy"],
        target: "https://external-content.duckduckgo.com",
        changeOrigin: true,
        pathRewrite: { "^/ddg-proxy": "" },
        secure: true,
        followRedirects: true,
        headers: { Referer: "https://duckduckgo.com/", "User-Agent": UA },
    },
];