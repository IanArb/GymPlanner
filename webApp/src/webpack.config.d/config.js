const TerserPlugin = require("terser-webpack-plugin");

config.optimization = config.optimization || {};
config.optimization.minimize = true;
config.optimization.minimizer = [
    new TerserPlugin({
        terserOptions: {
            mangle: true,    // Note: By default, mangle is set to true.
            compress: false, // Disable the transformations that reduce the code size.
            output: {
                beautify: false,
            },
        },
    }),
];

// Dev-only proxy: westwood.ie does not send CORS headers, so route image
// requests through the dev server, which is not subject to browser CORS.
// Production builds skip this and will need a backend image proxy.
console.log("[webpack.config.d] applying /img-proxy rule");
config.devServer = config.devServer || {};
config.devServer.proxy = {
    "/img-proxy": {
        target: "https://westwood.ie",
        changeOrigin: true,
        pathRewrite: { "^/img-proxy": "" },
        secure: true,
        followRedirects: true,
        headers: {
            Referer: "https://westwood.ie/",
            "User-Agent":
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
        },
    },
};