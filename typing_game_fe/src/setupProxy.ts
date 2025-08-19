import { createProxyMiddleware } from "http-proxy-middleware";
import { Express } from "express";

export default function setupProxy(app: Express) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: process.env.NEXT_PUBLIC_API_URL,
      changeOrigin: true,
    })
  );
}